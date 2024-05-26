package com.university.mcmaster.models.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestedVisitingSchedule {
//    private long startTime;
//    private long endTime;
    private String timeZone;
    private int durationInMin;
    private String date;
    private RequestedTimeSlot timeSlot;
}
