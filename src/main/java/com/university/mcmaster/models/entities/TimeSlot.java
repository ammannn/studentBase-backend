package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.DayPeriod;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeSlot {
    private DayPeriod dayPeriod;
    private Time start;
    private Time end;
}
