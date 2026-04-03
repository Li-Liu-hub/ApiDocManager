package com.apidoc.controller;

import com.apidoc.common.exception.BusinessException;
import com.apidoc.common.result.Result;
import com.apidoc.dto.DebugRequestDTO;
import com.apidoc.dto.DebugResponseDTO;
import com.apidoc.service.DebugService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@Validated
public class DebugController {

    private final DebugService debugService;

    @PostMapping("/send")
    public Result<DebugResponseDTO> send(@RequestBody @Validated DebugRequestDTO request) {
        if (request.getUrl() == null || request.getUrl().isBlank()) {
            throw new BusinessException("请求URL不能为空");
        }
        if (request.getMethod() == null || request.getMethod().isBlank()) {
            throw new BusinessException("请求方法不能为空");
        }

        DebugResponseDTO response = debugService.sendRequest(request);
        return Result.success(response);
    }
}
