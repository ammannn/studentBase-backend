package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.models.entities.RentalUnitFeatures;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchRentalUnitRequestDto {
    private RentalUnitFeatures features;
    private String country;
    private String state;
    private String city;
    private long minRent;
    private long maxRent;
}
