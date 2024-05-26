package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.models.entities.RequestedTimeSlot;
import com.university.mcmaster.models.entities.Time;
import com.university.mcmaster.models.entities.TimeSlotCommonProps;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestedTimeSlotForRentalUnit implements TimeSlotCommonProps {
    private Time start;
    private Time end;
    private int allowedAttendees;
}
