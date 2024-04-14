package com.university.mcmaster.services.impl;

import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.exceptions.ActionNotAllowedException;
import com.university.mcmaster.exceptions.EntityNotFoundException;
import com.university.mcmaster.exceptions.MissingRequiredParamException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.CreateApplicationRequestDto;
import com.university.mcmaster.models.entities.Application;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.repositories.ApplicationRepo;
import com.university.mcmaster.services.ApplicationService;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepo applicationRepo;

    @Override
    public ResponseEntity<?> getApplications(String requestId, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<?> createApplication(CreateApplicationRequestDto requestDto, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || null == userDetails.getRoles() || false == userDetails.getRoles().contains(UserRole.student)) throw new UnAuthenticatedUserException();
        Application application = Application.builder()
                .id(UUID.randomUUID().toString())
                .rentalUnitId(requestDto.getRentalUnitId())
                .students(Arrays.asList(userDetails.getId()))
                .createdBy(userDetails.getId())
                .applicationStatus(ApplicationStatus.tour_requested)
                .createdOn(Instant.now().toEpochMilli())
                .lastUpdatedOn(Instant.now().toEpochMilli())
                .build();
        applicationRepo.save(application);
        return ResponseEntity.status(200).body(ApiResponse.builder()
                        .msg("added applications")
                .build());
    }

    @Override
    public ResponseEntity<?> updateApplication(String applicationId, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || null == userDetails.getRoles() || false == userDetails.getRoles().contains(UserRole.student)) throw new UnAuthenticatedUserException();
        if(null == applicationId || applicationId.trim().isEmpty()) throw new MissingRequiredParamException();
        Application application = applicationRepo.findById(applicationId);
        if(null == application) throw new EntityNotFoundException();
//        if(userDetails.getRoles().contains(UserRole.rental_unit_owner)){
//            return updateApplicationForRentalUnitOwner(userDetails,application,requestId);
//        }
//        if(userDetails.getRoles().contains(UserRole.student)){
//            return updateApplicationForStudent(userDetails,application,requestId);
//        }
        throw new ActionNotAllowedException("update_application_status","invalid user role",401);
    }

//    private ResponseEntity<?> updateApplicationForStudent(CustomUserDetails userDetails,ApplicationStatus applicationStatus, Application application, String requestId) {
//        return null;
//    }
//
//    private ResponseEntity<?> updateApplicationForRentalUnitOwner(CustomUserDetails userDetails, Application application, String requestId) {
//    }

    @Override
    public ResponseEntity<?> deleteApplication(String applicationId, String requestId, HttpServletRequest request) {
        return null;
    }
}
