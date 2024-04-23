package com.university.mcmaster.controllers;

import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.models.dtos.request.CreateApplicationRequestDto;
import com.university.mcmaster.services.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/applications")
    public ResponseEntity<?> getApplications(
            @RequestParam(name = "status") ApplicationStatus status,
            @RequestParam(name = "rentalUnitId",required = false) String rentalUnitId,
            @RequestParam(name = "lastSeen",required = false) String lastSeen,
            @RequestParam(name = "limit",required = false) int limit,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.getApplications(status,rentalUnitId,lastSeen,limit,requestId,request);
    }

    @PostMapping("/applications")
    public ResponseEntity<?> createApplication(
            @RequestBody CreateApplicationRequestDto requestDto,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.createApplication(requestDto,requestId,request);
    }

    @PutMapping("/applications/{applicationId}")
    public ResponseEntity<?> updateApplication(
            @PathVariable("applicationId") String applicationId,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.updateApplication(applicationId,requestId,request);
    }

    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable("applicationId") String applicationId,
            @RequestParam("status") ApplicationStatus status,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.updateApplicationStatus(applicationId,status,requestId,request);
    }

    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<?> deleteApplication(
            @PathVariable("applicationId") String applicationId,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.deleteApplication(applicationId,requestId,request);
    }
}
