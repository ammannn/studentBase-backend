package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.models.entities.Address;
import com.university.mcmaster.models.entities.RentalUnitFeatures;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddUpdateRentalUnitRequestDto {
    private Address address;
    private long deposit;
    private long rent;
    private RentalUnitFeatures features;
    private String posterImageId;
}
