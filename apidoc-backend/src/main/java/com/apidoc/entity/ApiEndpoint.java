package com.apidoc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("api_endpoint")
public class ApiEndpoint {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long groupId;

    private Long projectId;

    private String method;

    private String path;

    private String summary;

    private String description;

    private String requestHeaders;

    private String requestParams;

    private String requestBody;

    private String responseBody;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
