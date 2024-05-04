package com.university.mcmaster.services.impl;

import com.university.mcmaster.enums.RentalUnitStage;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.exceptions.EntityNotFoundException;
import com.university.mcmaster.exceptions.MissingRequiredParamException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.RentalUnit;
import com.university.mcmaster.models.entities.User;
import com.university.mcmaster.services.AdminService;
import com.university.mcmaster.services.RentalUnitService;
import com.university.mcmaster.services.UserService;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserService userService;
    private final RentalUnitService rentalUnitService;

    @Override
    public ResponseEntity<?> getUsers(VerificationStatus verificationStatus,int limit,String lastSeen, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        log.trace("request received to get users by admin [requestId : "+requestId+"]");
        if(null == userDetails || (false == userDetails.getRoles().contains(UserRole.admin) && false == userDetails.getRoles().contains(UserRole.platform_admin))) throw new UnAuthenticatedUserException();
        List<User> users = userService.getPaginatedUsersByVerificationStatusForAdmin(verificationStatus,limit,lastSeen);
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(users)
                        .status(200)
                .build());
    }

    @Override
    public ResponseEntity<?> getRentalUnits(VerificationStatus verificationStatus,int limit,String lastSeen, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || (false == userDetails.getRoles().contains(UserRole.admin) && false == userDetails.getRoles().contains(UserRole.platform_admin))) throw new UnAuthenticatedUserException();
        List<RentalUnit> rentalUnits = rentalUnitService.getPaginatedRentalUnitsByVerificationStatusAndDeletedFalseForAdmin(verificationStatus,limit,lastSeen);
        return ResponseEntity.ok(ApiResponse.builder()
                .data(rentalUnits)
                .status(200)
                .build());
    }

    @Override
    public ResponseEntity<?> updateRentalUnitsStatus(String rentalUnitId, VerificationStatus verificationStatus, String reason, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || (false == userDetails.getRoles().contains(UserRole.admin) && false == userDetails.getRoles().contains(UserRole.platform_admin))) throw new UnAuthenticatedUserException();
        if(null == rentalUnitId || rentalUnitId.trim().isEmpty()) throw new MissingRequiredParamException();
        RentalUnit rentalUnit = rentalUnitService.findRentalUnitById(rentalUnitId);
        if(null == rentalUnit) throw new EntityNotFoundException();
        rentalUnitService.updateRentalUnit(rentalUnitId,new HashMap<String, Object>(){{
            put("verificationStatus",verificationStatus);
            if(VerificationStatus.verified == verificationStatus){
                put("stage", RentalUnitStage.listing_approved);
            }
            put("reason",reason);
        }});
        return ResponseEntity.ok(ApiResponse.builder()
                        .status(200)
                        .msg("updated status")
                .build());
    }

    @Override
    public ResponseEntity<?> updateUserStatus(String userId, VerificationStatus verificationStatus, String reason, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || (false == userDetails.getRoles().contains(UserRole.admin) && false == userDetails.getRoles().contains(UserRole.platform_admin))) throw new UnAuthenticatedUserException();
        if(null == userId || userId.trim().isEmpty()) throw new MissingRequiredParamException();
        User user = userService.findUserById(userId);
        if(null == user) throw new EntityNotFoundException();
        userService.updateUser(userId,new HashMap<String, Object>(){{
            put("verificationStatus",verificationStatus);
            put("reason",reason);
        }});
        return ResponseEntity.ok(ApiResponse.builder()
                .status(200)
                .msg("updated status")
                .build());
    }
}
