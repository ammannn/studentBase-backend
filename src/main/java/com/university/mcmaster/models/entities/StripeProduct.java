package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.Currency;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StripeProduct {
    private String id;
    private String productName;
    private String stripeProductId;
    private String stripePriceId;
    private long amount;
    private Currency unit;
    private long createdOn;
    private boolean deleted;
}
