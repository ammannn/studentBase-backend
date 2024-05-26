package com.university.mcmaster.controllers;

import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.CreateUpdateVisitingScheduleRequestDto;
import com.university.mcmaster.services.CalendarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @PostMapping("/schedule")
    ResponseEntity<ApiResponse> createVisitingSchedule(
            @RequestBody CreateUpdateVisitingScheduleRequestDto requestDto,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request){
        return calendarService.createVisitingScheduleObj(requestDto,requestId,request);
    }

    @GetMapping("/schedule")
    ResponseEntity<?> getVisitingSchedule(
            @RequestParam(name = "ownerId",required = false) String ownerId,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request){
        return calendarService.getVisitingSchedule(ownerId,requestId,request);
    }

    @GetMapping("/schedule/template")
    ResponseEntity<?> getVisitingScheduleTemplate(@RequestParam(name = "month") Month month,
                                                  @RequestHeader("requestId") String requestId,
                                                  HttpServletRequest request){
        return calendarService.getVisitingScheduleTemplate(month,requestId,request);
    }

    @PutMapping("/schedule")
    ResponseEntity<?> updateVisitingSchedule(@RequestBody CreateUpdateVisitingScheduleRequestDto requestDto,
                                             @RequestHeader("requestId") String requestId,
                                             HttpServletRequest request){
        return calendarService.updateVisitingSchedule(requestDto,requestId,request);
    }

}
