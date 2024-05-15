package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.PaymentRequestType;
import com.university.mcmaster.enums.PaymentStatus;
import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment implements FirebaseCommonProps {

    public static String collectionName = FirestoreConstants.FS_PAYMENTS;

    private String id;
    private long createdOn;
    private String userId;
    private String rentalUnitId;
    private String applicationId;
    private PaymentStatus paymentStatus;
    private StripeMetaData stripeMetaData;
    private PaymentRequestType type;
    private boolean eligibleForListing;
    private Amount amount;

    @Override
    public String getCollection() {
        return collectionName;
    }

    @Override
    public String getDbPath() {
        return "";
    }
}
