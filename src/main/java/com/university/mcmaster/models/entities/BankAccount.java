package com.university.mcmaster.models.entities;

import com.university.mcmaster.models.FirebaseCommonProps;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccount implements FirebaseCommonProps {
    public static String collectionName = "bank_accounts";
    private String id;
    private String userId;
    private String paymentGatewayAccountId;
    private long createdOn;

    @Override
    public String getCollection() {
        return collectionName;
    }

    @Override
    public String getDbPath() {
        return "";
    }
}
