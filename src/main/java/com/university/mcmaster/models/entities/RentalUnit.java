package com.university.mcmaster.models.entities;

import com.google.cloud.firestore.Firestore;
import com.university.mcmaster.enums.RentalUnitStatus;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

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
    private boolean delete;
    private VerificationStatus verificationStatus;
    private Address address;
    private RentalUnitFeatures features;
    private RentalUnitStatus rentalUnitStatus;
    private long createdOn;
    private long lastUpdatedOn;
    private String userId;
    private String posterImageId;
    private String posterImagePath;

    @Override
    public String getCollection() {
        return collection;
    }

    @Override
    public String getDbPath() {
        return "";
    }
}
