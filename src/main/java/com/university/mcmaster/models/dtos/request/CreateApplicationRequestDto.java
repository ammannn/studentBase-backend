package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.models.entities.Time;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateApplicationRequestDto {
    private String rentalUnitId;
    private String date;
//    private Time visitStartTime;
//    private Time visitEndTime;
    private String timeZone;
}
