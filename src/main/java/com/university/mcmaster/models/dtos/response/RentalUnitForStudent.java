package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.RentalUnitStatus;
import com.university.mcmaster.models.entities.Address;
import com.university.mcmaster.models.entities.RentalUnitFeatures;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalUnitForStudent {
    private long rent;
    private long deposit;
    private Address address;
    private RentalUnitFeatures features;
    private RentalUnitStatus rentalUnitStatus;
    private String posterImageUrl;
}
