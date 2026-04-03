package com.apidoc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("api_group")
public class ApiGroup {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long projectId;

    private String name;

    private Integer sortOrder;

    private LocalDateTime createdAt;
}
