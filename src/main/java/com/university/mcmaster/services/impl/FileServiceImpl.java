package com.university.mcmaster.services.impl;

import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.enums.RentalUnitElement;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.exceptions.*;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.GetUploadUrlForFileRequestDto;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.File;
import com.university.mcmaster.models.entities.StudentDocFile;
import com.university.mcmaster.repositories.FileRepo;
import com.university.mcmaster.services.FileService;
import com.university.mcmaster.services.RentalUnitService;
import com.university.mcmaster.services.UserService;
import com.university.mcmaster.utils.Constants;
import com.university.mcmaster.utils.GcpStorageUtil;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    private final FileRepo fileRepo;
    private final UserService userService;
    private final RentalUnitService rentalUnitService;

    @Override
    public ResponseEntity<ApiResponse<?>> getUploadUrlForFile(GetUploadUrlForFileRequestDto requestDto, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        if(null == requestDto.getFilePurpose()) throw new MissingRequiredParamException("purpose");
        if(null == requestDto.getContentType() || requestDto.getContentType().trim().isEmpty()) throw new MissingRequiredParamException("content_type");
        String rentalUnitId = null;
        if(userDetails.getRoles().contains(UserRole.student)){
            if(false == FilePurpose.isValidFilePurpose(UserRole.student,requestDto.getFilePurpose())) throw new InvalidParamValueException("filePurpose",FilePurpose.validForStudent().toString());
            requestDto.setRentalUnitElement(null);
        }
        if(userDetails.getRoles().contains(UserRole.rental_unit_owner)){
            if(false == FilePurpose.isValidFilePurpose(UserRole.rental_unit_owner,requestDto.getFilePurpose())) throw new InvalidParamValueException("filePurpose",FilePurpose.validForStudent().toString());
            if(null == requestDto.getRentalUnitId() || requestDto.getRentalUnitId().isEmpty()) throw new MissingRequiredParamException("rentalUnitId");
            if(null == requestDto.getRentalUnitElement()) requestDto.setRentalUnitElement(RentalUnitElement.others);
            rentalUnitId = requestDto.getRentalUnitId().trim();
            if(FilePurpose.rental_unit_image == requestDto.getFilePurpose()) {
                List<File> files = fileRepo.getFilesByRentalUnitIdAndRentalUnitElementDeletedFalseAndUploadedOnGcpTrue(rentalUnitId,requestDto.getRentalUnitElement());
                if(files.size() >= requestDto.getRentalUnitElement().getAllowedFiles()) throw new ActionNotAllowedException("upload_image","maximum allowed images per rental unit element '"+ requestDto.getRentalUnitElement().toString()+"' is " + requestDto.getRentalUnitElement().getAllowedFiles(),400);
            }
        }
        String fileId = UUID.randomUUID().toString();
        String path = userDetails.getId() + "/" + requestDto.getFilePurpose().toString() + "/" + fileId;
        File file = File.builder()
                .id(fileId)
                .fileName(requestDto.getFileName())
                .createdOn(Instant.now().toEpochMilli())
                .userId(userDetails.getId())
                .filePath(path)
                .purpose(requestDto.getFilePurpose())
                .rentalUnitId(rentalUnitId)
                .rentalUnitElement(requestDto.getRentalUnitElement())
                .build();
        fileRepo.save(file);
        URL url = GcpStorageUtil.createPostUrl(path,requestDto.getContentType());
        return ResponseEntity.status(200).body(ApiResponse.builder()
                        .status(200)
                        .data(new HashMap<String,Object>(){{
                            put("fileId",fileId);
                            put("url",url.toString());
                        }}).build());
    }

    @Override
    public ResponseEntity<ApiResponse<?>> confirmFileUpload(String fileId,String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        if(null == fileId || fileId.isEmpty()) throw new MissingRequiredParamException("fileId");
        File file = fileRepo.findById(fileId);
        if(null == file || false == userDetails.getId().equals(file.getUserId())) throw new EntityNotFoundException();
        fileRepo.update(fileId,new HashMap<String,Object>(){{
            put("uploadedOnGcp",true);
        }});
        if(file.getPurpose().isProfileFile()) {
            userService.updateUser(userDetails.getId(),new HashMap<String,Object>(){{
                put("documentPaths."+file.getPurpose().toString(), StudentDocFile.builder()
                        .path(file.getFilePath())
                        .name(file.getFileName())
                        .build());
            }});
        }else if(null != file.getRentalUnitId() && FilePurpose.rental_unit_poster_image == file.getPurpose()){
            rentalUnitService.updateRentalUnitPosterImage(file.getRentalUnitId(),file.getId(),file.getFilePath());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                        .status(200)
                        .data(GcpStorageUtil.createGetUrl(file.getFilePath()))
                .build());
    }

    @Override
    public ResponseEntity<?> deleteFile(String fileId, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        if(null == fileId || fileId.isEmpty()) throw new MissingRequiredParamException("fileId");
        File file = fileRepo.findById(fileId);
        if(null != file){
            if(false == userDetails.getId().equals(file.getUserId())) throw new ActionNotAllowedException("delete_file","user can delete file uploaded by himself",400);
            fileRepo.update(fileId,new HashMap<String, Object>(){{
                put("deleted",true);
            }});
        }
        return ResponseEntity.ok(ApiResponse.builder()
                        .status(200)
                        .msg("deleted file")
                .build());
    }

    @Override
    public File getFileById(String imageId) {
        return fileRepo.findById(imageId);
    }

    @Override
    public List<File> getFilesByRentalUnitIdAndUploadedOnGcpTrueAndDeletedFalse(String id) {
        return fileRepo.getFilesByRentalUnitIdAndUploadedOnGcpTrueAndDeletedFalse(id);
    }
}
