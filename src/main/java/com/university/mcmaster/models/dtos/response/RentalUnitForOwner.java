package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.RentalUnitStatus;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.entities.Address;
import com.university.mcmaster.models.entities.RentalUnitFeatures;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class RentalUnitForOwner {
    private String rentalUnitId;
    private long rent;
    private long deposit;
    private VerificationStatus verificationStatus;
    private Address address;
    private RentalUnitFeatures features;
    private RentalUnitStatus rentalUnitStatus;
    private long createdOn;
    private String posterImageUrl;
    private double avgRating;
    private int likes;
}
