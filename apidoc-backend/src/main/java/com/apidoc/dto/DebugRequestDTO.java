package com.apidoc.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DebugRequestDTO {
    private String method;
    private String url;
    private Map<String, String> headers;
    private Map<String, String> params;
    private String body;
}
