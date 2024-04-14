package com.university.mcmaster.models.dtos.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateApplicationRequestDto {
    private String rentalUnitId;
}
