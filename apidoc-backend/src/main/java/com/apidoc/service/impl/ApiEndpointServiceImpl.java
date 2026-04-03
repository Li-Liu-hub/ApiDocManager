package com.apidoc.service.impl;

import com.apidoc.common.exception.BusinessException;
import com.apidoc.entity.ApiEndpoint;
import com.apidoc.mapper.ApiEndpointMapper;
import com.apidoc.service.ApiEndpointService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiEndpointServiceImpl implements ApiEndpointService {

    private final ApiEndpointMapper apiEndpointMapper;

    @Override
    public IPage<ApiEndpoint> page(Long projectId, Long groupId, String method, int current, int size) {
        LambdaQueryWrapper<ApiEndpoint> wrapper = new LambdaQueryWrapper<>();

        if (projectId != null) {
            wrapper.eq(ApiEndpoint::getProjectId, projectId);
        }
        if (groupId != null) {
            wrapper.eq(ApiEndpoint::getGroupId, groupId);
        }
        if (method != null && !method.isEmpty()) {
            wrapper.eq(ApiEndpoint::getMethod, method);
        }

        wrapper.orderByDesc(ApiEndpoint::getCreatedAt);

        return apiEndpointMapper.selectPage(new Page<>(current, size), wrapper);
    }

    @Override
    public List<ApiEndpoint> listByProjectId(Long projectId) {
        return apiEndpointMapper.selectList(new LambdaQueryWrapper<ApiEndpoint>()
                .eq(ApiEndpoint::getProjectId, projectId)
                .orderByDesc(ApiEndpoint::getCreatedAt));
    }

    @Override
    public List<ApiEndpoint> listByGroupId(Long groupId) {
        return apiEndpointMapper.selectList(new LambdaQueryWrapper<ApiEndpoint>()
                .eq(ApiEndpoint::getGroupId, groupId)
                .orderByDesc(ApiEndpoint::getCreatedAt));
    }

    @Override
    public ApiEndpoint getById(Long id) {
        ApiEndpoint apiEndpoint = apiEndpointMapper.selectById(id);
        if (apiEndpoint == null) {
            throw new BusinessException("Endpoint not found");
        }
        return apiEndpoint;
    }

    @Override
    public ApiEndpoint create(ApiEndpoint apiEndpoint) {
        // 校验同项目下 method + path 唯一
        Long count = apiEndpointMapper.selectCount(new LambdaQueryWrapper<ApiEndpoint>()
                .eq(ApiEndpoint::getProjectId, apiEndpoint.getProjectId())
                .eq(ApiEndpoint::getMethod, apiEndpoint.getMethod())
                .eq(ApiEndpoint::getPath, apiEndpoint.getPath()));
        if (count > 0) {
            throw new BusinessException("Endpoint with same method and path already exists in this project");
        }

        apiEndpoint.setCreatedAt(LocalDateTime.now());
        apiEndpoint.setUpdatedAt(LocalDateTime.now());
        apiEndpointMapper.insert(apiEndpoint);
        return apiEndpoint;
    }

    @Override
    public ApiEndpoint update(ApiEndpoint apiEndpoint) {
        ApiEndpoint existing = getById(apiEndpoint.getId());

        // 校验同项目下 method + path 唯一（排除自身）
        Long count = apiEndpointMapper.selectCount(new LambdaQueryWrapper<ApiEndpoint>()
                .eq(ApiEndpoint::getProjectId, apiEndpoint.getProjectId())
                .eq(ApiEndpoint::getMethod, apiEndpoint.getMethod())
                .eq(ApiEndpoint::getPath, apiEndpoint.getPath())
                .ne(ApiEndpoint::getId, apiEndpoint.getId()));
        if (count > 0) {
            throw new BusinessException("Endpoint with same method and path already exists in this project");
        }

        existing.setGroupId(apiEndpoint.getGroupId());
        existing.setMethod(apiEndpoint.getMethod());
        existing.setPath(apiEndpoint.getPath());
        existing.setSummary(apiEndpoint.getSummary());
        existing.setDescription(apiEndpoint.getDescription());
        existing.setRequestHeaders(apiEndpoint.getRequestHeaders());
        existing.setRequestParams(apiEndpoint.getRequestParams());
        existing.setRequestBody(apiEndpoint.getRequestBody());
        existing.setResponseBody(apiEndpoint.getResponseBody());
        existing.setStatus(apiEndpoint.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());

        apiEndpointMapper.updateById(existing);
        return existing;
    }

    @Override
    public void delete(Long id) {
        apiEndpointMapper.deleteById(id);
    }
}
