package com.university.mcmaster.models.entities;

import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Day {
    private DayOfWeek dayOfWeek;
    private List<TimeSlot> timeSlots;
}
