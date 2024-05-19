package com.university.mcmaster.models.dtos.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateApplicationRequestDto {
    private String date;
    private String timeZone;
}
