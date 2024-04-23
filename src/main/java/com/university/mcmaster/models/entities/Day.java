package com.university.mcmaster.models.entities;

import com.university.mcmaster.utils.Utility;
import lombok.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Day {
    private DayOfWeek dayOfWeek;
    private List<TimeSlot> timeSlots;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return getDayOfWeek() == day.getDayOfWeek() && Utility.areListsEqual(getTimeSlots(), day.getTimeSlots());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDayOfWeek(), getTimeSlots());
    }
}
