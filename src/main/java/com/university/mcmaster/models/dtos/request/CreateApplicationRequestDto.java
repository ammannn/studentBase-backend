package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.models.entities.Time;
import com.university.mcmaster.models.entities.VisitingSchedule;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateApplicationRequestDto {
    private String rentalUnitId;
    private String date;
    private int visitDurationInMin;
    private Time time;
    private String timeZone;
}
