package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.PaymentStatus;
import com.university.mcmaster.enums.RentalUnitStatus;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalUnit implements FirebaseCommonProps {
    private static String collection = FirestoreConstants.FS_RENTAL_UNITS;
    private String id;
    private Amount rent;
    private Amount deposit;
    private boolean deleted;
    private VerificationStatus verificationStatus;
    private Address address;
    private RentalUnitFeatures features;
//    availability status
    private RentalUnitStatus rentalUnitStatus;
//    private RentalUnitStage stage;
    private long createdOn;
    private long lastUpdatedOn;
    private String userId;
    private String posterImageId;
    private String posterImagePath;
    private Map<String,Object> customFields;
    private Map<String, Integer> rating;
    private Contact contact;
    private String title;
    private String description;
    private String leaseTerm;
    private long leaseStartDate;
    private List<String> featureSearchList;
    private String reason;
    private Map<String,Integer> counts;
    private StripeProduct stripeProduct;
    private boolean eligibleForListing; // payment_status = successfull and verification_status = verified
    private PaymentStatus paymentStatus;
    private String organizationName;
    private String sheerIdOrganizationId;
    private int remainingBeds;
    private VisitingSchedule visitingSchedule;
    private Map<String,Integer> bookedSlotsCounts;

    @Override
    public String getCollection() {
        return collection;
    }

    @Override
    public String getDbPath() {
        return "";
    }
}
