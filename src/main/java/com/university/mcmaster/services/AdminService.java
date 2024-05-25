package com.university.mcmaster.services;

import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.integrations.sheerid.model.SheerIdVerificationDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<?> getUsers(VerificationStatus verificationStatus,int limit,String lastSeen, String requestId, HttpServletRequest request);

    ResponseEntity<?> getRentalUnits(VerificationStatus verificationStatus,int limit,String lastSeen,  String requestId, HttpServletRequest request);

    ResponseEntity<?> updateRentalUnitsStatus(String rentalUnitId, VerificationStatus verificationStatus,String reason, String requestId, HttpServletRequest request);

    ResponseEntity<?> updateUserStatus(String userId, VerificationStatus verificationStatus,String reason, String requestId, HttpServletRequest request);

    ResponseEntity<?> addStudentSheerIdData(SheerIdVerificationDetails details, String requestId, HttpServletRequest request);
}
