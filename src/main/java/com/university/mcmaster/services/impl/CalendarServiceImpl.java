package com.university.mcmaster.services.impl;

import com.university.mcmaster.enums.DayPeriod;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.exceptions.EntityNotFoundException;
import com.university.mcmaster.exceptions.InvalidParamValueException;
import com.university.mcmaster.exceptions.MissingRequiredParamException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.CreateUpdateVisitingScheduleRequestDto;
import com.university.mcmaster.models.dtos.response.DayResponse;
import com.university.mcmaster.models.dtos.response.TimeSlotResponse;
import com.university.mcmaster.models.entities.*;
import com.university.mcmaster.repositories.CalendarRepo;
import com.university.mcmaster.services.ApplicationService;
import com.university.mcmaster.services.CalendarService;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepo calendarRepo;
    private final ApplicationService applicationService;

    @Override
    public ResponseEntity<ApiResponse> createVisitingSchedule(CreateUpdateVisitingScheduleRequestDto requestDto, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || null != userDetails.getRoles() || false == userDetails.getRoles().contains(UserRole.rental_unit_owner)) throw new UnAuthenticatedUserException();
        verifyVisitingSchedule(requestDto);
        VisitingSchedule schedule = VisitingSchedule.builder()
                .id(UUID.randomUUID().toString())
                .userId(userDetails.getId())
                .createdOn(Instant.now().toEpochMilli())
                .days(requestDto.getDays())
                .timeZone(requestDto.getTimeZone())
                .build();
        boolean isSaved = calendarRepo.save(schedule);
        return ResponseEntity.ok(ApiResponse.builder()
                        .data("created schedule")
                .build());
    }

    private void verifyVisitingSchedule(CreateUpdateVisitingScheduleRequestDto requestDto) {
        if(null == requestDto.getTimeZone() || requestDto.getTimeZone().trim().isEmpty()) throw new MissingRequiredParamException("timeZone");
        try {
            ZoneId.of(requestDto.getTimeZone());
        }catch (Exception e){
            throw new InvalidParamValueException("timeZone");
        }
        if(null == requestDto.getDays() || requestDto.getDays().isEmpty()) throw new MissingRequiredParamException("days");
    }

    @Override
    public ResponseEntity<?> getVisitingSchedule(String ownerId,String requestId,HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || null == userDetails.getRoles()) throw new UnAuthenticatedUserException();
        if(userDetails.getRoles().contains(UserRole.student)){
            if(null == ownerId || ownerId.trim().isEmpty()) throw new UnAuthenticatedUserException();
        } else if(userDetails.getRoles().contains(UserRole.rental_unit_owner)){
            if(null == ownerId || ownerId.trim().isEmpty()) ownerId = userDetails.getId();
        }
        VisitingSchedule schedule = calendarRepo.findByUserId(ownerId);
        Map<LocalDate,DayResponse> res = generateVisitingSchedule(schedule,ownerId,Utility.getCurrentMonth(schedule.getTimeZone()),
                2024,schedule.getTimeZone(),false);
        return ResponseEntity.ok(ApiResponse.builder()
                .data(res)
                .build());
    }

    @Override
    public ResponseEntity<?> getVisitingScheduleTemplate(Month month,String requestId,HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || null == userDetails.getRoles() || false == userDetails.getRoles().contains(UserRole.rental_unit_owner)) throw new UnAuthenticatedUserException();
        Map<LocalDate,DayResponse> res = generateVisitingSchedule(null,userDetails.getId(),month,
                2024,null,true);
        return ResponseEntity.ok(ApiResponse.builder()
                .data(res)
                .build());
    }

    @Override
    public ResponseEntity<?> updateVisitingSchedule(CreateUpdateVisitingScheduleRequestDto requestDto, String requestId, HttpServletRequest request){
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || null == userDetails.getRoles() || false == userDetails.getRoles().contains(UserRole.rental_unit_owner)) throw new UnAuthenticatedUserException();
        VisitingSchedule schedule = calendarRepo.findByUserId(userDetails.getId());
        if(null == schedule) throw new EntityNotFoundException();
        Map<String,Object> updateMap = new HashMap<>();
        if(null != requestDto.getTimeZone() && false == requestDto.getTimeZone().trim().isEmpty() && false == requestDto.getTimeZone().equals(schedule.getTimeZone())){
            updateMap.put("timeZone",requestDto.getTimeZone());
        }
        if(null != requestDto.getDays() && false == Utility.areListsEqual(requestDto.getDays(),schedule.getDays())){
            updateMap.put("days",requestDto.getDays());
        }
        if(false == updateMap.isEmpty()){
            calendarRepo.update(schedule.getId(),updateMap);
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .data("updated schedule")
                .build());
    }


    private List<LocalDate> generateDates(Month month, int year) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate date = LocalDate.of(year, month, 1);
        while (date.getMonth() == month) {
            dates.add(date);
            date = date.plusDays(1);
        }
        return dates;
    }

    private Map<LocalDate, DayResponse> generateVisitingSchedule(VisitingSchedule scheduleObj,String userId,Month month, int year, String timeZone,boolean template) {
        List<LocalDate> dates = generateDates(month, year);
        Map<LocalDate, DayResponse> schedule = new HashMap<>();
        if(null == scheduleObj) template = true;
        Set<String> selectedDayOfWeek = new HashSet<>(){{
            if(null != scheduleObj){
                for (Day day : scheduleObj.getDays()) {
                    add(day.getDayOfWeek().toString());
                }
            }
        }};
        for (LocalDate date : dates) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if(false == template && false == selectedDayOfWeek.contains(dayOfWeek.toString())) continue;
            DayResponse day = new DayResponse();
            day.setDayOfWeek(dayOfWeek);
            boolean finalTemplate = template;
            day.setTimeSlots(new ArrayList<>(){{
                for (int i = 10; i < 19; i++) {
                    TimeSlotResponse timeSlot = TimeSlotResponse.builder()
                            .start(Time.builder()
                                    .hour(i > 12 ? i-12 : i).build())
                            .end(Time.builder()
                                    .hour(i > 12 ? i-12+1 : i).build())
                            .build();
                    timeSlot.getEnd().setDayPeriod(timeSlot.getEnd().getHour() >= 12 ? DayPeriod.PM : DayPeriod.AM);
                    timeSlot.getStart().setDayPeriod(timeSlot.getStart().getHour() >= 12 ? DayPeriod.PM : DayPeriod.AM);
                    timeSlot.setAvailable(finalTemplate ? true : applicationService.isVistingTimeSlotAvailble(userId,date,timeZone,timeSlot.getStart(),timeSlot.getEnd()));
                    add(timeSlot);
                }
            }});
            schedule.put(date, day);
        }
        return schedule;
    }
}
