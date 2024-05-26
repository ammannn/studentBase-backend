package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.models.entities.RequestedTimeSlot;
import com.university.mcmaster.models.entities.Time;
import com.university.mcmaster.models.entities.TimeSlot;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateApplicationRequestDto {
    private String rentalUnitId;
    private String date;
    private RequestedTimeSlot timeSlot;
    private String timeZone;

}
