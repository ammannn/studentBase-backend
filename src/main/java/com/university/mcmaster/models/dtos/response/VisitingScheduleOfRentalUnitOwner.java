package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.models.entities.Day;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitingScheduleOfRentalUnitOwner {
    private List<Day> days;
    private String timeZone;
}
