package com.apidoc.service.impl;

import com.apidoc.common.exception.BusinessException;
import com.apidoc.entity.ApiEndpoint;
import com.apidoc.entity.ApiGroup;
import com.apidoc.entity.Project;
import com.apidoc.mapper.ApiEndpointMapper;
import com.apidoc.mapper.ApiGroupMapper;
import com.apidoc.mapper.ProjectMapper;
import com.apidoc.service.ProjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final ApiGroupMapper apiGroupMapper;
    private final ApiEndpointMapper apiEndpointMapper;

    @Override
    public List<Project> list() {
        return projectMapper.selectList(new LambdaQueryWrapper<Project>().orderByDesc(Project::getCreatedAt));
    }

    @Override
    public Project getById(Long id) {
        Project project = projectMapper.selectById(id);
        if (project == null) {
            throw new BusinessException("Project not found");
        }
        return project;
    }

    @Override
    public Project create(Project project) {
        // 校验项目名称唯一
        Long count = projectMapper.selectCount(new LambdaQueryWrapper<Project>()
                .eq(Project::getName, project.getName()));
        if (count > 0) {
            throw new BusinessException("Project name already exists");
        }

        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        projectMapper.insert(project);
        return project;
    }

    @Override
    public Project update(Project project) {
        Project existing = getById(project.getId());

        // 校验名称唯一（排除自身）
        Long count = projectMapper.selectCount(new LambdaQueryWrapper<Project>()
                .eq(Project::getName, project.getName())
                .ne(Project::getId, project.getId()));
        if (count > 0) {
            throw new BusinessException("Project name already exists");
        }

        existing.setName(project.getName());
        existing.setDescription(project.getDescription());
        existing.setBaseUrl(project.getBaseUrl());
        existing.setStatus(project.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());

        projectMapper.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 级联删除该项目下所有分组和接口
        List<ApiGroup> groups = apiGroupMapper.selectList(
                new LambdaQueryWrapper<ApiGroup>().eq(ApiGroup::getProjectId, id));

        for (ApiGroup group : groups) {
            apiEndpointMapper.delete(new LambdaQueryWrapper<ApiEndpoint>()
                    .eq(ApiEndpoint::getGroupId, group.getId()));
        }

        apiGroupMapper.delete(new LambdaQueryWrapper<ApiGroup>().eq(ApiGroup::getProjectId, id));
        projectMapper.deleteById(id);
    }
}
