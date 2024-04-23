package com.university.mcmaster.services.impl;

import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.CreateUpdateVisitingScheduleRequestDto;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.Day;
import com.university.mcmaster.models.entities.VisitingSchedule;
import com.university.mcmaster.repositories.CalendarRepo;
import com.university.mcmaster.services.CalendarService;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepo calendarRepo;

    public ResponseEntity<ApiResponse> createVisitingSchedule(CreateUpdateVisitingScheduleRequestDto requestDto, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails) throw new UnAuthenticatedUserException();
        verifyVisitingSchedule(requestDto);
        return null;
    }

    private void verifyVisitingSchedule(CreateUpdateVisitingScheduleRequestDto requestDto) {
    }

//    public ResponseEntity<?> getVisitingSchedule(){
//        VisitingSchedule schedule = VisitingSchedule.builder().build();
//
//    }


    public static List<LocalDate> generateDates(Month month, int year, String timeZone) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate date = LocalDate.of(year, month, 1);
        while (date.getMonth() == month) {
            dates.add(date);
            date = date.plusDays(1);
        }
        return dates;
    }

    public static Map<LocalDate, Day> generateVisitingSchedule(Month month, int year, String timeZone) {
        List<LocalDate> dates = generateDates(month, year, timeZone);
        Map<LocalDate, Day> schedule = new HashMap<>();
        for (LocalDate date : dates) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            Day day = new Day();
            day.setDayOfWeek(dayOfWeek);
            schedule.put(date, day);
        }
        return schedule;
    }

    public static void main(String[] args) {
        Month month = Month.APRIL;
        int year = 2024;
        String timeZone = "Asia/Kolkata";
        Map<LocalDate, Day> visitingSchedule = generateVisitingSchedule(month, year, timeZone);
        visitingSchedule.forEach((date, day) -> System.out.println(date + " : " + day.getDayOfWeek()));
    }
}
