package com.apidoc.controller;

import com.apidoc.common.result.Result;
import com.apidoc.entity.ApiEndpoint;
import com.apidoc.service.ApiEndpointService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/endpoint")
@RequiredArgsConstructor
public class ApiEndpointController {

    private final ApiEndpointService apiEndpointService;

    @GetMapping("/page")
    public Result<IPage<ApiEndpoint>> page(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false) String method,
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(apiEndpointService.page(projectId, groupId, method, current, size));
    }

    @GetMapping("/list/{projectId}")
    public Result<List<ApiEndpoint>> listByProjectId(@PathVariable Long projectId) {
        return Result.success(apiEndpointService.listByProjectId(projectId));
    }

    @GetMapping("/{id}")
    public Result<ApiEndpoint> getById(@PathVariable Long id) {
        return Result.success(apiEndpointService.getById(id));
    }

    @PostMapping
    public Result<ApiEndpoint> create(@RequestBody ApiEndpoint apiEndpoint) {
        return Result.success(apiEndpointService.create(apiEndpoint));
    }

    @PutMapping("/{id}")
    public Result<ApiEndpoint> update(@PathVariable Long id, @RequestBody ApiEndpoint apiEndpoint) {
        apiEndpoint.setId(id);
        return Result.success(apiEndpointService.update(apiEndpoint));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        apiEndpointService.delete(id);
        return Result.success();
    }
}
