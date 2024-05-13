package com.university.mcmaster.models.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StripeMetaData {
    private String paymentIntentId;
    private String checkoutSessionId;
}
