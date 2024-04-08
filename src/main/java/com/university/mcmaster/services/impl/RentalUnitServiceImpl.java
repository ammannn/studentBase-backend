package com.university.mcmaster.services.impl;

import com.university.mcmaster.enums.FileType;
import com.university.mcmaster.enums.RentalUnitStatus;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.exceptions.ActionNotAllowedException;
import com.university.mcmaster.exceptions.EntityNotFoundException;
import com.university.mcmaster.exceptions.MissingRequiredParamException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.models.dtos.request.AddUpdateRentalUnitRequestDto;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.response.RentalUnitForOwner;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.File;
import com.university.mcmaster.models.entities.RentalUnit;
import com.university.mcmaster.repositories.RentalUnitRepo;
import com.university.mcmaster.services.FileService;
import com.university.mcmaster.services.RentalUnitService;
import com.university.mcmaster.utils.GcpStorageUtil;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalUnitServiceImpl implements RentalUnitService {

    private final RentalUnitRepo rentalUnitRepo;
    @Autowired
    @Lazy
    private FileService fileService;

    @Override
    public ResponseEntity<ApiResponse<?>> getRentalUnits(int limit,String lastSeen,String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        if(userDetails.getRoles().contains(UserRole.student.name())){
            return getRentalUnitsForStudent(userDetails,limit,lastSeen,requestId);
        }
        if(userDetails.getRoles().contains(UserRole.rental_unit_owner.name())){
            return getRentalUnitsForRentalUnitOwner(userDetails,limit,lastSeen,requestId);
        }
        throw new ActionNotAllowedException("get_rental_units","user is not registered either as student or rental unit owner");
    }

    private ResponseEntity<ApiResponse<?>> getRentalUnitsForRentalUnitOwner(CustomUserDetails userDetails, int limit, String lastSeen, String requestId) {
        List<RentalUnit> rentalUnits = rentalUnitRepo.getRentalUnitByUserIdAndDeletedFalse(userDetails.getId(),limit,lastSeen);
        List<RentalUnitForOwner> res = new ArrayList<>();
        for (RentalUnit rentalUnit : rentalUnits) {
            String url = null;
            if(null != rentalUnit.getPosterImagePath())url = GcpStorageUtil.createGetUrl(rentalUnit.getPosterImagePath()).toString();
            res.add(RentalUnitForOwner.builder()
                            .rentalUnitStatus(rentalUnit.getRentalUnitStatus())
                            .rentalUnitId(rentalUnit.getId())
                            .rent(rentalUnit.getRent())
                            .deposit(rentalUnit.getDeposit())
                            .verificationStatus(rentalUnit.getVerificationStatus())
                            .address(rentalUnit.getAddress())
                            .features(rentalUnit.getFeatures())
                            .rentalUnitStatus(rentalUnit.getRentalUnitStatus())
                            .createdOn(rentalUnit.getCreatedOn())
                            .posterImageUrl(url)
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                        .status(200)
                        .data(res)
                .build());
    }

    private ResponseEntity<ApiResponse<?>> getRentalUnitsForStudent(CustomUserDetails userDetails,int limit,String lastSeen, String requestId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> addRentalUnit(AddUpdateRentalUnitRequestDto requestDto, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || false == userDetails.getRoles().contains(UserRole.rental_unit_owner)) throw new UnAuthenticatedUserException();
        validateCreateRentalPropertyRequest(requestDto);
        RentalUnit rentalUnit = RentalUnit.builder()
                .id(UUID.randomUUID().toString())
                .userId(userDetails.getId())
                .rent(requestDto.getRent())
                .deposit(requestDto.getDeposit())
                .verificationStatus(VerificationStatus.pending)
                .address(requestDto.getAddress())
                .features(requestDto.getFeatures())
                .createdOn(Instant.now().toEpochMilli())
                .lastUpdatedOn(Instant.now().toEpochMilli())
                .rentalUnitStatus(RentalUnitStatus.available)
                .build();
        rentalUnitRepo.save(rentalUnit);
        return ResponseEntity.ok(ApiResponse.builder()
                        .status(200)
                        .msg("added rental unit")
                .build());
    }

    private void validateCreateRentalPropertyRequest(AddUpdateRentalUnitRequestDto requestDto) {
        List<String> missingProps = new ArrayList<>();
        if(null == requestDto.getAddress())missingProps.add("address");
        else validateAddress(requestDto,missingProps);
        if(null == requestDto.getFeatures()) missingProps.add("features");
        else validateFeatures(requestDto,missingProps);
        if(false == missingProps.isEmpty()) throw new MissingRequiredParamException(missingProps.toString());
    }

    private void validateFeatures(AddUpdateRentalUnitRequestDto requestDto, List<String> missingProps) {
        if(0 == requestDto.getFeatures().getRooms()) missingProps.add("rooms");
        if(0 == requestDto.getFeatures().getAreaInSqFt()) missingProps.add("areaInSqFt");
        if(null == requestDto.getFeatures().getAmenities() || requestDto.getFeatures().getAmenities().isEmpty()) missingProps.add("amenities");
    }

    private void validateAddress(AddUpdateRentalUnitRequestDto requestDto, List<String> missingProps) {
        String country = Optional.ofNullable(requestDto.getAddress().getCountry()).map(s->s.trim().toLowerCase()).orElse("");
        String state = Optional.ofNullable(requestDto.getAddress().getState()).map(s->s.trim().toLowerCase()).orElse("");
        String zip = Optional.ofNullable(requestDto.getAddress().getZip()).map(s->s.trim().toLowerCase()).orElse("");
        String city = Optional.ofNullable(requestDto.getAddress().getCity()).map(s->s.trim().toLowerCase()).orElse("");
        if(country.isEmpty()) missingProps.add("country");
        if(state.isEmpty()) missingProps.add("state");
        if(zip.isEmpty()) missingProps.add("zip");
        if(city.isEmpty()) missingProps.add("city");
        requestDto.getAddress().setCity(city);
        requestDto.getAddress().setCountry(country);
        requestDto.getAddress().setZip(zip);
        requestDto.getAddress().setState(state);
    }

    @Override
    public ResponseEntity<ApiResponse<?>> updateRentalUnits(String rentalUnitId,AddUpdateRentalUnitRequestDto requestDto,
                                                            String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || false == userDetails.getRoles().contains(UserRole.rental_unit_owner)) throw new UnAuthenticatedUserException();
        if(null == rentalUnitId || rentalUnitId.trim().isEmpty()) throw new MissingRequiredParamException("rentalUnitId");
        RentalUnit rentalUnit = rentalUnitRepo.findById(rentalUnitId);
        if(null == rentalUnit || false == userDetails.getId().equals(rentalUnit.getUserId())) throw new EntityNotFoundException();
        return updateRentalUnitsUnAuth(userDetails.getId(),rentalUnit,false,requestDto,requestId);
    }

    private ResponseEntity<ApiResponse<?>> updateRentalUnitsUnAuth(String userId, RentalUnit rentalUnit, boolean isAdmin, AddUpdateRentalUnitRequestDto requestDto, String requestId) {
//        address , deposit , rent , features , posterImageId
        Map<String,Object> updateMap = new HashMap<>();
        List<String> missingProps = new ArrayList<>();
        if(null != requestDto.getAddress() && false == requestDto.getAddress().equals(rentalUnit.getAddress())){
            validateAddress(requestDto,missingProps);
            if(false == missingProps.isEmpty()) throw new MissingRequiredParamException(missingProps.toString());
            updateMap.put("address",requestDto.getAddress());
        }

        if(0 != requestDto.getRent() && requestDto.getRent() != rentalUnit.getRent()){
            updateMap.put("rent",requestDto.getRent());
        }
        if(0 != requestDto.getDeposit() && requestDto.getDeposit() != rentalUnit.getDeposit()){
            updateMap.put("deposit",requestDto.getDeposit());
        }
        if(null != requestDto.getFeatures() && false == requestDto.getFeatures().equals(rentalUnit.getFeatures())){
            validateFeatures(requestDto,missingProps);
            if(false == missingProps.isEmpty()) throw new MissingRequiredParamException(missingProps.toString());
            updateMap.put("features",requestDto.getFeatures());
        }
        if(null != requestDto.getPosterImageId() && false == requestDto.getPosterImageId().trim().isEmpty() && false == requestDto.getPosterImageId().trim().equals(rentalUnit.getPosterImageId())){
            File file = fileService.getFileById(requestDto.getPosterImageId());
            if(null == file) throw new EntityNotFoundException();
            if(false ==  userId.equals(file.getUserId())) throw new ActionNotAllowedException("update_rental_unit","user tried to use image uploaded by other users");
            updateMap.put("posterImageId",requestDto.getPosterImageId());
            updateMap.put("posterImagePath",file.getFilePath());
        }
        if(false == updateMap.isEmpty()) rentalUnitRepo.update(rentalUnit.getId(),updateMap);
        return ResponseEntity.ok(ApiResponse.builder()
                        .status(200)
                        .msg("updated rental unit")
                .build());
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteRentalUnits(String rentalUnitId, String requestId, HttpServletRequest request) {
        return null;
    }

    @Override
    public void updateRentalUnitPosterImage(String rentalUnitId, String posterImageId,String imagePath) {
        rentalUnitRepo.update(rentalUnitId,new HashMap<String,Object>(){{
            put("posterImageId",posterImageId);
            put("posterImagePath",imagePath);
        }});
    }

    @Override
    public List<RentalUnit> getPaginatedRentalUnitsByVerificationStatusAndDeletedFalseForAdmin(VerificationStatus verificationStatus, int limit, String lastSeen) {
       List<RentalUnit> rentalUnits = rentalUnitRepo.getPaginatedRentalUnitsByVerificationStatusAndDeletedFalseForAdmin(verificationStatus,limit,lastSeen);
        for (RentalUnit rentalUnit : rentalUnits) {
            rentalUnit.setCustomFields(new HashMap<>());
            if(null != rentalUnit.getPosterImagePath()) rentalUnit.getCustomFields().put("posterImagePath",GcpStorageUtil.createGetUrl(rentalUnit.getPosterImagePath()));
            List<File> files = fileService.getFilesByRentalUnitIdAndUploadedOnGcpTrueAndDeletedFalse(rentalUnit.getId());
            rentalUnit.getCustomFields().put("images",files.stream().map(f->new HashMap<String,Object>(){{
                put("imageId",f.getId());
                put("url",GcpStorageUtil.createGetUrl(f.getFilePath()));
            }}).collect(Collectors.toList()));
        }
       return rentalUnits;
    }

    @Override
    public RentalUnit findRentalUnitById(String rentalUnitId) {
        return rentalUnitRepo.findById(rentalUnitId);
    }

    @Override
    public void updateRentalUnit(String rentalUnitId, HashMap<String, Object> updateMap) {
        rentalUnitRepo.update(rentalUnitId,updateMap);
    }
}
