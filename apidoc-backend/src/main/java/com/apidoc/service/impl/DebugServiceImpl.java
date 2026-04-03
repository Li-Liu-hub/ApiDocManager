package com.apidoc.service.impl;

import com.apidoc.dto.DebugRequestDTO;
import com.apidoc.dto.DebugResponseDTO;
import com.apidoc.service.DebugService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DebugServiceImpl implements DebugService {

    private final RestTemplate restTemplate;

    @Override
    public DebugResponseDTO sendRequest(DebugRequestDTO request) {
        long startTime = System.currentTimeMillis();

        try {
            // 构建URL，拼接query参数
            String url = buildUrlWithParams(request.getUrl(), request.getParams());

            // 设置请求头
            HttpHeaders httpHeaders = new HttpHeaders();
            if (request.getHeaders() != null) {
                request.getHeaders().forEach(httpHeaders::set);
            }

            // 设置请求体
            HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod().toUpperCase());
            HttpEntity<String> entity = null;

            if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT) {
                entity = new HttpEntity<>(request.getBody(), httpHeaders);
            } else {
                entity = new HttpEntity<>(httpHeaders);
            }

            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    httpMethod,
                    entity,
                    String.class
            );

            long duration = System.currentTimeMillis() - startTime;

            // 提取响应头
            Map<String, String> responseHeaders = new HashMap<>();
            response.getHeaders().forEach((key, values) -> {
                responseHeaders.put(key, String.join(", ", values));
            });

            // 构建响应
            DebugResponseDTO responseDTO = new DebugResponseDTO();
            responseDTO.setStatusCode(response.getStatusCode().value());
            responseDTO.setHeaders(responseHeaders);
            responseDTO.setBody(response.getBody());
            responseDTO.setDuration(duration);

            return responseDTO;

        } catch (HttpClientErrorException e) {
            // 4xx 客户端错误
            long duration = System.currentTimeMillis() - startTime;
            DebugResponseDTO responseDTO = new DebugResponseDTO();
            responseDTO.setStatusCode(e.getStatusCode().value());
            responseDTO.setBody(e.getResponseBodyAsString());
            responseDTO.setDuration(duration);
            return responseDTO;
        } catch (HttpServerErrorException e) {
            // 5xx 服务器错误
            long duration = System.currentTimeMillis() - startTime;
            DebugResponseDTO responseDTO = new DebugResponseDTO();
            responseDTO.setStatusCode(e.getStatusCode().value());
            responseDTO.setBody(e.getResponseBodyAsString());
            responseDTO.setDuration(duration);
            return responseDTO;
        } catch (RestClientException e) {
            // 处理超时和连接错误
            long duration = System.currentTimeMillis() - startTime;
            DebugResponseDTO responseDTO = new DebugResponseDTO();
            responseDTO.setDuration(duration);

            String message = e.getMessage();
            if (message != null && message.contains("Connection refused")) {
                responseDTO.setStatusCode(0);
                responseDTO.setBody("无法连接到目标服务器");
            } else if (message != null && (message.contains("Timeout") || message.contains("timeout"))) {
                responseDTO.setStatusCode(0);
                responseDTO.setBody("请求超时");
            } else {
                responseDTO.setStatusCode(0);
                responseDTO.setBody("请求失败: " + e.getMessage());
            }
            return responseDTO;
        }
    }

    private String buildUrlWithParams(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        boolean hasQuery = url.contains("?");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null && !key.isEmpty()) {
                sb.append(hasQuery ? "&" : "?");
                sb.append(key);
                if (value != null && !value.isEmpty()) {
                    sb.append("=").append(value);
                }
                hasQuery = true;
            }
        }

        return sb.toString();
    }
}
