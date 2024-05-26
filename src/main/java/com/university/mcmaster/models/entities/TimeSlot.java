package com.university.mcmaster.models.entities;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeSlot implements TimeSlotCommonProps{
    private Time start;
    private Time end;
    private int allowedAttendees;
    private int booked;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(getStart(), timeSlot.getStart()) && Objects.equals(getEnd(), timeSlot.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStart(), getEnd());
    }
}
