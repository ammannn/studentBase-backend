package com.university.mcmaster.controllers;

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
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.getApplications(requestId,request);
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

    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<?> deleteApplication(
            @PathVariable("applicationId") String applicationId,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.deleteApplication(applicationId,requestId,request);
    }
}
