package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.models.entities.RequestedTimeSlot;
import com.university.mcmaster.models.entities.TimeSlot;
import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestedDay {
    private DayOfWeek dayOfWeek;
    private String date;
    private List<RequestedTimeSlotForRentalUnit> timeSlots;
}
