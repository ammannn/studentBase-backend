package com.university.mcmaster.services;

import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.CreateUpdateVisitingScheduleRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.time.Month;

public interface CalendarService {
    ResponseEntity<ApiResponse> createVisitingSchedule(CreateUpdateVisitingScheduleRequestDto requestDto, String requestId, HttpServletRequest request);

    ResponseEntity<?> getVisitingSchedule(String ownerId, String requestId, HttpServletRequest request);

    ResponseEntity<?> getVisitingScheduleTemplate(Month month, String requestId, HttpServletRequest request);

    ResponseEntity<?> updateVisitingSchedule(CreateUpdateVisitingScheduleRequestDto requestDto, String requestId, HttpServletRequest request);
}
