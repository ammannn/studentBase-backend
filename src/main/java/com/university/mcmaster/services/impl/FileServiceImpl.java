package com.university.mcmaster.services.impl;

import com.university.mcmaster.enums.*;
import com.university.mcmaster.exceptions.*;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.GetUploadUrlForFileRequestDto;
import com.university.mcmaster.models.entities.*;
import com.university.mcmaster.repositories.FileRepo;
import com.university.mcmaster.services.ApplicationService;
import com.university.mcmaster.services.FileService;
import com.university.mcmaster.services.RentalUnitService;
import com.university.mcmaster.services.UserService;
import com.university.mcmaster.utils.GcpStorageUtil;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    private final FileRepo fileRepo;
    private final UserService userService;
    private final RentalUnitService rentalUnitService;
    @Lazy
    @Autowired
    private ApplicationService applicationService;

    @Override
    public ResponseEntity<ApiResponse<?>> getUploadUrlForFile(GetUploadUrlForFileRequestDto requestDto, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        String rentalUnitId = requestDto.getRentalUnitId();
        String applicationId = requestDto.getApplicationId();
        verifyFileUploadRequest(rentalUnitId,userDetails,requestDto,applicationId,"add");
        return getUploadUrlForFileUnAuth(userDetails,requestDto,requestId,rentalUnitId,applicationId);
    }

    private void verifyFileUploadRequest(String rentalUnitId,CustomUserDetails userDetails, GetUploadUrlForFileRequestDto requestDto,String applicationId,String action) {
        if(null == requestDto.getFilePurpose()) throw new MissingRequiredParamException("purpose");
        if(null == requestDto.getContentType() || requestDto.getContentType().trim().isEmpty()) throw new MissingRequiredParamException("content_type");
        if(FilePurpose.user_profile_image == requestDto.getFilePurpose()) return;
        if(userDetails.getRoles().contains(UserRole.student)){
            if(false == FilePurpose.isValidFilePurpose(UserRole.student,requestDto.getFilePurpose())) throw new InvalidParamValueException("filePurpose",FilePurpose.validForStudent().toString());
            if(FilePurpose.signed_lease_doc == requestDto.getFilePurpose()){
                if(null == applicationId || applicationId.isEmpty()) throw new MissingRequiredParamException("applicationId");
                Application application = applicationService.getApplicationById(applicationId);
                if(null == application) throw new EntityNotFoundException();
                if(ApplicationStatus.lease_offered != application.getApplicationStatus()) throw new ActionNotAllowedException("offer_lease","lease doc cannot be signed untill it is offered from landlord");
                rentalUnitId = application.getRentalUnitId();
            }else{
                applicationId = null;
                rentalUnitId = null;
            }
            requestDto.setRentalUnitElement(null);

        }else if(userDetails.getRoles().contains(UserRole.rental_unit_owner)){
            if(false == FilePurpose.isValidFilePurpose(UserRole.rental_unit_owner,requestDto.getFilePurpose())) throw new InvalidParamValueException("filePurpose",FilePurpose.validForStudent().toString());
            applicationId = null;
            if(FilePurpose.lease_doc == requestDto.getFilePurpose()){
                rentalUnitId = null;
            }else{
                if(null == rentalUnitId || rentalUnitId.isEmpty()) throw new MissingRequiredParamException("rentalUnitId");
                if(null == requestDto.getRentalUnitElement()) requestDto.setRentalUnitElement(RentalUnitElement.others);
                if(FilePurpose.rental_unit_image == requestDto.getFilePurpose()) {
                    List<File> files = fileRepo.getFilesByRentalUnitIdAndRentalUnitElementDeletedFalseAndUploadedOnGcpTrue(rentalUnitId,requestDto.getRentalUnitElement());
                    if(files.size() >= requestDto.getRentalUnitElement().getAllowedFiles() && "add".equals(action)) throw new ActionNotAllowedException("upload_image","maximum allowed images per rental unit element '"+ requestDto.getRentalUnitElement().toString()+"' is " + requestDto.getRentalUnitElement().getAllowedFiles(),400);
                }
            }
        }
    }

    private ResponseEntity<ApiResponse<?>> getUploadUrlForFileUnAuth(CustomUserDetails userDetails, GetUploadUrlForFileRequestDto requestDto, String requestId,String rentalUnitId,String applicationId) {
        String fileId = UUID.randomUUID().toString();
        String path = userDetails.getId() + "/" + requestDto.getFilePurpose().toString() + "/" + fileId;
        File file = File.builder()
                .id(fileId)
                .fileName(requestDto.getFileName())
                .createdOn(Instant.now().toEpochMilli())
                .userId(userDetails.getId())
                .filePath(path)
                .purpose(requestDto.getFilePurpose())
                .applicationId(applicationId)
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
        if(FilePurpose.user_profile_image == file.getPurpose()){
            userService.updateUser(userDetails.getId(),new HashMap<String,Object>(){{
                put("profileImage", StudentDocFile.builder()
                        .path(file.getFilePath())
                        .name(file.getFileName())
                        .build());
            }});
        } else if(file.getPurpose().isProfileFile()) {
            userService.updateUser(userDetails.getId(),new HashMap<String,Object>(){{
                put("documentPaths."+file.getPurpose().toString(), StudentDocFile.builder()
                        .path(file.getFilePath())
                        .name(file.getFileName())
                        .build());
            }});
        }else if(FilePurpose.rental_unit_poster_image == file.getPurpose() && null != file.getRentalUnitId()){
            rentalUnitService.updateRentalUnitPosterImage(file.getRentalUnitId(),file.getId(),file.getFilePath());
        }else if(FilePurpose.signed_lease_doc == file.getPurpose()){
            applicationService.updateApplication(file.getApplicationId(),new HashMap<String,Object>(){{
                put("applicationStatus",ApplicationStatus.lease_signed);
                put("signedLeaseDetails", SignedLeaseDetails.builder()
                        .signedOn(Instant.now().toEpochMilli())
                        .fileId(fileId)
                        .filePath(file.getFilePath())
                        .build());
            }});
            rentalUnitService.updateRentalUnit(file.getRentalUnitId(),new HashMap<String, Object>(){{
                put("RentalUnitStage", RentalUnitStage.lease_signed);
            }});
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

    @Override
    public ResponseEntity<?> replaceFile(String fileId, GetUploadUrlForFileRequestDto requestDto, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        if(null == fileId || fileId.trim().isEmpty()) throw new MissingRequiredParamException();
        File file = fileRepo.findById(fileId);
        if(null == file) throw new EntityNotFoundException();
        if(false == userDetails.getId().equals(file.getUserId())) throw new ActionNotAllowedException("replace_file","user can only update file uploaded by them self",403);
        if(file.getPurpose() != requestDto.getFilePurpose()) throw new ActionNotAllowedException("replace_file","new file and existing file have different purposes",400);
        String rentalUnitId = requestDto.getRentalUnitId();
        String applicationId = requestDto.getApplicationId();
        verifyFileUploadRequest(rentalUnitId,userDetails,requestDto,applicationId,"replace");
        fileRepo.update(fileId, new HashMap<String,Object>(){{
            put("deleted",true);
        }});
        return getUploadUrlForFileUnAuth(userDetails,requestDto,requestId,rentalUnitId,applicationId);
    }

    @Override
    public ResponseEntity<?> getLeaseDocuments(String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        List<File> files = fileRepo.getFilesByUserIdAndPurposeLeaseDocAndDeletedFalse(userDetails.getId());
        List<Map<String,Object>> res = new ArrayList<>();
        for (File file : files) {
            res.add(new HashMap<String,Object>(){{
                put("fileId",file.getId());
                put("url",GcpStorageUtil.createGetUrl(file.getFilePath()));
                put("fileName",file.getFileName());
            }});
        }
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(res)
                .build());
    }
}
