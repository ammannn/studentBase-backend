package com.university.mcmaster.controllers;

import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.GetUploadUrlForFileRequestDto;
import com.university.mcmaster.services.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/files")
    public ResponseEntity<ApiResponse<?>> getUploadUrlForFile(
            @RequestBody GetUploadUrlForFileRequestDto requestDro,
            @RequestParam("requestId") String requestId,
            HttpServletRequest request
    ){
        return fileService.getUploadUrlForFile(requestDro,requestId,request);
    }

    @PostMapping("/files/{fileId}/cnfm")
    public ResponseEntity<ApiResponse<?>> confirmFileUpload(
            @PathVariable("fileId") String fileId,
            HttpServletRequest request
    ){
        return fileService.confirmFileUpload(fileId,request);
    }
}
