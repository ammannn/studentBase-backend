package com.university.mcmaster.models.entities;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestedTimeSlot {
    private Time start;
    private Time end;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestedTimeSlot timeSlot = (RequestedTimeSlot) o;
        return Objects.equals(getStart(), timeSlot.getStart()) && Objects.equals(getEnd(), timeSlot.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStart(), getEnd());
    }
}
