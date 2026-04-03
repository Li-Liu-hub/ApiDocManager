package com.apidoc.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DebugResponseDTO {
    private Integer statusCode;
    private Map<String, String> headers;
    private String body;
    private Long duration;
}
