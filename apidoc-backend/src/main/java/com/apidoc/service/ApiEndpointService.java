package com.apidoc.service;

import com.apidoc.entity.ApiEndpoint;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface ApiEndpointService {
    IPage<ApiEndpoint> page(Long projectId, Long groupId, String method, int current, int size);

    java.util.List<ApiEndpoint> listByProjectId(Long projectId);

    java.util.List<ApiEndpoint> listByGroupId(Long groupId);

    ApiEndpoint getById(Long id);

    ApiEndpoint create(ApiEndpoint apiEndpoint);

    ApiEndpoint update(ApiEndpoint apiEndpoint);

    void delete(Long id);
}
