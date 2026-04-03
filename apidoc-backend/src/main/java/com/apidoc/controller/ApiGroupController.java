package com.apidoc.controller;

import com.apidoc.common.result.Result;
import com.apidoc.entity.ApiGroup;
import com.apidoc.service.ApiGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class ApiGroupController {

    private final ApiGroupService apiGroupService;

    @GetMapping("/list/{projectId}")
    public Result<List<ApiGroup>> listByProjectId(@PathVariable Long projectId) {
        return Result.success(apiGroupService.listByProjectId(projectId));
    }

    @GetMapping("/{id}")
    public Result<ApiGroup> getById(@PathVariable Long id) {
        return Result.success(apiGroupService.getById(id));
    }

    @PostMapping
    public Result<ApiGroup> create(@RequestBody ApiGroup apiGroup) {
        return Result.success(apiGroupService.create(apiGroup));
    }

    @PutMapping("/{id}")
    public Result<ApiGroup> update(@PathVariable Long id, @RequestBody ApiGroup apiGroup) {
        apiGroup.setId(id);
        return Result.success(apiGroupService.update(apiGroup));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        apiGroupService.delete(id);
        return Result.success();
    }
}
