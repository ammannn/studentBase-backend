package com.university.mcmaster.services;

import com.university.mcmaster.models.dtos.request.CreateApplicationRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ApplicationService {
    ResponseEntity<?> getApplications(String requestId, HttpServletRequest request);

    ResponseEntity<?> createApplication(CreateApplicationRequestDto requestDto, String requestId, HttpServletRequest request);

    ResponseEntity<?> updateApplication(String applicationId, String requestId, HttpServletRequest request);

    ResponseEntity<?> deleteApplication(String applicationId, String requestId, HttpServletRequest request);
}
