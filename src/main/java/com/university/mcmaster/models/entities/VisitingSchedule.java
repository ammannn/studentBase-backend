package com.university.mcmaster.models.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitingSchedule {
    private List<Day> days;
    private String timeZone;
}
