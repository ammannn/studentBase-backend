package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.models.entities.Day;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUpdateVisitingScheduleRequestDto {
    private List<Day> days;
    private String timeZone;
}
