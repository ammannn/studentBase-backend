package com.university.mcmaster.services;

import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.CreateUpdateVisitingScheduleRequestDto;
import com.university.mcmaster.models.entities.VisitingSchedule;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.time.Month;

public interface CalendarService {
    ResponseEntity<ApiResponse> createVisitingScheduleObj(CreateUpdateVisitingScheduleRequestDto requestDto, String requestId, HttpServletRequest request);

    VisitingSchedule createVisitingScheduleObj(String userId, CreateUpdateVisitingScheduleRequestDto requestDto, String requestId);

    ResponseEntity<?> getVisitingSchedule(String ownerId, String requestId, HttpServletRequest request);

    ResponseEntity<?> getVisitingScheduleTemplate(Month month, String requestId, HttpServletRequest request);

    ResponseEntity<?> updateVisitingSchedule(CreateUpdateVisitingScheduleRequestDto requestDto, String requestId, HttpServletRequest request);
}
