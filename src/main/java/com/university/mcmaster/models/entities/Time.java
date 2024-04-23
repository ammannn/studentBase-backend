package com.university.mcmaster.models.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Time {
    private int hour;
    private int minute;
}
