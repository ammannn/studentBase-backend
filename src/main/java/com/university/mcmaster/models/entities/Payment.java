package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.PaymentRequestType;
import com.university.mcmaster.enums.PaymentStatus;
import com.university.mcmaster.models.FirebaseCommonProps;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment implements FirebaseCommonProps {

    public static String collectionName = "payments";

    private String id;
    private long createdOn;
    private String userId;
    private String rentalUnitId;
    private PaymentStatus paymentStatus;
    private StripeMetaData stripeMetaData;
    private PaymentRequestType type;

    @Override
    public String getCollection() {
        return collectionName;
    }

    @Override
    public String getDbPath() {
        return "";
    }
}
