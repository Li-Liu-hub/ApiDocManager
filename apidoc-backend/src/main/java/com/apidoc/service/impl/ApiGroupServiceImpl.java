package com.apidoc.service.impl;

import com.apidoc.common.exception.BusinessException;
import com.apidoc.entity.ApiEndpoint;
import com.apidoc.entity.ApiGroup;
import com.apidoc.mapper.ApiEndpointMapper;
import com.apidoc.mapper.ApiGroupMapper;
import com.apidoc.service.ApiGroupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiGroupServiceImpl implements ApiGroupService {

    private final ApiGroupMapper apiGroupMapper;
    private final ApiEndpointMapper apiEndpointMapper;

    @Override
    public List<ApiGroup> listByProjectId(Long projectId) {
        return apiGroupMapper.selectList(new LambdaQueryWrapper<ApiGroup>()
                .eq(ApiGroup::getProjectId, projectId)
                .orderByAsc(ApiGroup::getSortOrder));
    }

    @Override
    public ApiGroup getById(Long id) {
        ApiGroup apiGroup = apiGroupMapper.selectById(id);
        if (apiGroup == null) {
            throw new BusinessException("Group not found");
        }
        return apiGroup;
    }

    @Override
    public ApiGroup create(ApiGroup apiGroup) {
        apiGroup.setCreatedAt(LocalDateTime.now());
        apiGroupMapper.insert(apiGroup);
        return apiGroup;
    }

    @Override
    public ApiGroup update(ApiGroup apiGroup) {
        ApiGroup existing = getById(apiGroup.getId());

        existing.setName(apiGroup.getName());
        existing.setSortOrder(apiGroup.getSortOrder());

        apiGroupMapper.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 级联删除该分组下所有接口
        apiEndpointMapper.delete(new LambdaQueryWrapper<ApiEndpoint>()
                .eq(ApiEndpoint::getGroupId, id));
        apiGroupMapper.deleteById(id);
    }
}
