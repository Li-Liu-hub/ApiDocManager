package com.apidoc.service;

import com.apidoc.dto.DebugRequestDTO;
import com.apidoc.dto.DebugResponseDTO;

public interface DebugService {
    DebugResponseDTO sendRequest(DebugRequestDTO request);
}
