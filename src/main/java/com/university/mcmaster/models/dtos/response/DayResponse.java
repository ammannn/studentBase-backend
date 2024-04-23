package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.models.entities.TimeSlot;
import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayResponse {
        private DayOfWeek dayOfWeek;
        private List<TimeSlotResponse> timeSlots;
}
