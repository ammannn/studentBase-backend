package com.university.mcmaster.services;

import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.GetUploadUrlForFileRequestDto;
import com.university.mcmaster.models.entities.File;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FileService {
    ResponseEntity<ApiResponse<?>> getUploadUrlForFile(GetUploadUrlForFileRequestDto requestDro, String requestId, HttpServletRequest request);
    ResponseEntity<ApiResponse<?>> confirmFileUpload(String fileId,String requestId, HttpServletRequest request);
    ResponseEntity<?> deleteFile(String fileId, String requestId, HttpServletRequest request);
    File getFileById(String posterImageId);
    List<File> getFilesByRentalUnitIdAndUploadedOnGcpTrueAndDeletedFalse(String id);
    ResponseEntity<?> replaceFile(String fileId, GetUploadUrlForFileRequestDto requestDto, String requestId, HttpServletRequest request);
}
