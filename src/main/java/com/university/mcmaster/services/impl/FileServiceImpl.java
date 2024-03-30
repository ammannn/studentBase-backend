package com.university.mcmaster.services.impl;

import com.university.mcmaster.exceptions.EntityNotFoundException;
import com.university.mcmaster.exceptions.MissingRequiredParamException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.GetUploadUrlForFileRequestDto;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.File;
import com.university.mcmaster.repositories.FileRepo;
import com.university.mcmaster.services.FileService;
import com.university.mcmaster.utils.GcpStorageUtil;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepo fileRepo;

    @Override
    public ResponseEntity<ApiResponse<?>> getUploadUrlForFile(GetUploadUrlForFileRequestDto requestDto, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        if(null == requestDto.getFilePurpose()) throw new MissingRequiredParamException("purpose");
        if(null == requestDto.getContentType() || requestDto.getContentType().trim().isEmpty()) throw new MissingRequiredParamException("content_type");
        String fileId = UUID.randomUUID().toString();
        String path = userDetails.getId() + "/" + requestDto.getFilePurpose().toString() + "/" + fileId;
        File file = File.builder()
                .id(fileId)
                .createdOn(Instant.now().toEpochMilli())
                .userId(userDetails.getId())
                .filePath(path)
                .purpose(requestDto.getFilePurpose())
                .build();
        fileRepo.save(file);
        URL url = GcpStorageUtil.createPostUrl(path,requestDto.getContentType());
        return ResponseEntity.status(200).body(ApiResponse.builder()
                        .status(200)
                        .data(url).build());
    }

    @Override
    public ResponseEntity<ApiResponse<?>> confirmFileUpload(String fileId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        if(null == fileId || fileId.isEmpty()) throw new MissingRequiredParamException("fileId");
        File file = fileRepo.findById(fileId);
        if(null == file || false == userDetails.getId().equals(file.getUserId())) throw new EntityNotFoundException();
        fileRepo.update(fileId,new HashMap<String,Object>(){{
            put("uploadedOnGcp",true);
        }});
        return ResponseEntity.ok(ApiResponse.builder()
                        .status(200)
                        .data(GcpStorageUtil.createGetUrl(file.getFilePath()))
                .build());
    }
}
