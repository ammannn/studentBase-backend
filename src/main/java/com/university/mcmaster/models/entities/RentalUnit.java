package com.university.mcmaster.models.entities;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Internal;
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
    private long rent;
    private long deposit;
    private boolean deleted;
    private VerificationStatus verificationStatus;
    private Address address;
    private RentalUnitFeatures features;
    private RentalUnitStatus rentalUnitStatus;
    private long createdOn;
    private long lastUpdatedOn;
    private String userId;
    private String posterImageId;
    private String posterImagePath;
    private Map<String,Object> customFields;
    private Map<String, Integer> rating;
    private int likes;
    private Contact contact;
    private String title;
    private String description;
    private int leaseTerm;
    private long leaseStartDate;
    private List<String> featureSearchList;
    private String reason;

    @Override
    public String getCollection() {
        return collection;
    }

    @Override
    public String getDbPath() {
        return "";
    }
}
