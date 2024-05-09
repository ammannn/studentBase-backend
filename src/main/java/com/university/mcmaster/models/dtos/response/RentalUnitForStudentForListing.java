package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.RentalUnitStatus;
import com.university.mcmaster.models.entities.Address;
import com.university.mcmaster.models.entities.Contact;
import com.university.mcmaster.models.entities.RentalUnitFeatures;
import com.university.mcmaster.models.entities.VisitingSchedule;
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
    private long rent;
    private long deposit;
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
    private VisitingScheduleOfRentalUnitOwner visitingSchedule;
}
