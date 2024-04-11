package com.university.mcmaster.services.impl;

import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.exceptions.ActionNotAllowedException;
import com.university.mcmaster.exceptions.EntityNotFoundException;
import com.university.mcmaster.exceptions.MissingRequiredParamException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.GetUploadUrlForFileRequestDto;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.File;
import com.university.mcmaster.repositories.FileRepo;
import com.university.mcmaster.services.FileService;
import com.university.mcmaster.services.RentalUnitService;
import com.university.mcmaster.services.UserService;
import com.university.mcmaster.utils.Constants;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

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
            if(FilePurpose.isValidFilePurpose(UserRole.student,requestDto.getFilePurpose())) throw new ActionNotAllowedException("upload_file","invalid file purpose",400);
        }
        if(userDetails.getRoles().contains(UserRole.rental_unit_owner)){
            if(FilePurpose.isValidFilePurpose(UserRole.rental_unit_owner,requestDto.getFilePurpose())) throw new ActionNotAllowedException("upload_file","invalid file purpose",400);
            if(null == requestDto.getRentalUnitId() || requestDto.getRentalUnitId().isEmpty()) throw new MissingRequiredParamException("rentalUnitId");
            rentalUnitId = requestDto.getRentalUnitId().trim();
            if(FilePurpose.rental_unit_image == requestDto.getFilePurpose()){
                List<File> files = fileRepo.getFilesByRentalUnitIdAndDeletedFalseAndUploadedOnGcpTrue(rentalUnitId);
                if(files.size() >= Constants.RENTAL_UNIT_IMAGES_LIMIT) throw new ActionNotAllowedException("upload_image","maximum allowed images per rental unit : " + Constants.RENTAL_UNIT_IMAGES_LIMIT,400);
            }
        }
        String fileId = UUID.randomUUID().toString();
        String path = userDetails.getId() + "/" + requestDto.getFilePurpose().toString() + "/" + fileId;
        File file = File.builder()
                .id(fileId)
                .createdOn(Instant.now().toEpochMilli())
                .userId(userDetails.getId())
                .filePath(path)
                .purpose(requestDto.getFilePurpose())
                .rentalUnitId(rentalUnitId)
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
                put("documentPaths."+file.getPurpose().toString(),file.getFilePath());
            }});
        }
        if(null != file.getRentalUnitId() && FilePurpose.rental_unit_poster_image == file.getPurpose()){
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
