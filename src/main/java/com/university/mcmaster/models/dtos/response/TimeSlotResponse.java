package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.DayPeriod;
import com.university.mcmaster.models.entities.Time;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeSlotResponse {
    private Time start;
    private Time end;
    private boolean available;
}
