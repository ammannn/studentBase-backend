package com.university.mcmaster.models.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestedVisitingSchedule {
    private long time;
    private String timeZone;
    private int durationInMin;
}
