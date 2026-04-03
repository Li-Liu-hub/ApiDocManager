package com.apidoc.service;

import com.apidoc.entity.ApiGroup;

import java.util.List;

public interface ApiGroupService {
    List<ApiGroup> listByProjectId(Long projectId);

    ApiGroup getById(Long id);

    ApiGroup create(ApiGroup apiGroup);

    ApiGroup update(ApiGroup apiGroup);

    void delete(Long id);
}
