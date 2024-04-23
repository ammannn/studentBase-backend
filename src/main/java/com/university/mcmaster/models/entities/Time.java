package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.DayPeriod;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Time {
    private DayPeriod dayPeriod;
    private int hour;
    private int minute;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return getHour() == time.getHour() && getMinute() == time.getMinute() && getDayPeriod() == time.getDayPeriod();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDayPeriod(), getHour(), getMinute());
    }
}
