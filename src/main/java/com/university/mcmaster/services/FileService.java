package com.university.mcmaster.services;

import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.GetUploadUrlForFileRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface FileService {
    ResponseEntity<ApiResponse<?>> getUploadUrlForFile(GetUploadUrlForFileRequestDto requestDro, String requestId, HttpServletRequest request);
    ResponseEntity<ApiResponse<?>> confirmFileUpload(String fileId, HttpServletRequest request);
}
