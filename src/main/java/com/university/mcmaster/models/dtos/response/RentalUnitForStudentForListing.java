package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.RentalUnitStatus;
import com.university.mcmaster.models.entities.*;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalUnitForStudentForListing {
    private String rentalUnitId;
    private String title;
    private String description;
    private String leaseTerm;
    private long leaseStartDate;
    private Amount rent;
    private Amount deposit;
    private int likes;
    private int reviews;
    private String review;
    private Address address;
    private RentalUnitFeatures features;
    private RentalUnitStatus rentalUnitStatus;
    private String posterImageUrl;
    private boolean liked;
    private double avgRating;
    private int givenRating;
    private Contact contact;
    private Map<String,List<HashMap<String,Object>>> images;
    private ApplicationForStudent application;
    private String organizationName;
    private int bedsRemaining;
    private VisitingSchedule visitingSchedule;
}
