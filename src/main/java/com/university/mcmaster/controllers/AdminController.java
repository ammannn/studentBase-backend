package com.university.mcmaster.controllers;

import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.integrations.sheerid.model.SheerIdVerificationDetails;
import com.university.mcmaster.services.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(
            @RequestParam(name = "verificationStatus",required = false,defaultValue = "pending") VerificationStatus verificationStatus,
            @RequestHeader("requestId") String requestId,
            @RequestParam(name = "limit",required = false,defaultValue = "50") int limit,
            @RequestParam(name = "lastSeen",required = false) String lastSeen,
            HttpServletRequest request
    ){
        return adminService.getUsers(verificationStatus,limit,lastSeen,requestId,request);
    }

    @GetMapping("/rental-units")
    public ResponseEntity<?> getRentalUnits(
            @RequestParam(name = "verificationStatus",required = false,defaultValue = "pending") VerificationStatus verificationStatus,
            @RequestHeader("requestId") String requestId,
            @RequestParam(name = "limit",required = false,defaultValue = "50") int limit,
            @RequestParam(name = "lastSeen",required = false) String lastSeen,
            HttpServletRequest request
    ){
        return adminService.getRentalUnits(verificationStatus,limit,lastSeen,requestId,request);
    }

    @PutMapping("/rental-units/{rentalUnitId}/status")
    public ResponseEntity<?> updateRentalUnitsStatus(
            @PathVariable("rentalUnitId") String rentalUnitId,
            @RequestParam(name = "verificationStatus",required = false,defaultValue = "pending") VerificationStatus verificationStatus,
            @RequestHeader("requestId") String requestId,
            @RequestParam(name = "reason",required = false) String reason,
            HttpServletRequest request
    ){
        return adminService.updateRentalUnitsStatus(rentalUnitId,verificationStatus,reason,requestId,request);
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable("userId") String userId,
            @RequestParam(name = "verificationStatus",required = false,defaultValue = "pending") VerificationStatus verificationStatus,
            @RequestHeader("requestId") String requestId,
            @RequestParam(name = "reason",required = false) String reason,
            HttpServletRequest request
    ){
        return adminService.updateUserStatus(userId,verificationStatus,reason,requestId,request);
    }

    @PostMapping("/sheer-id-data")
    public ResponseEntity<?> addStudentSheerIdData(
            @RequestBody SheerIdVerificationDetails details,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request

    ){
        return adminService.addStudentSheerIdData(details,requestId,request);
    }
}
