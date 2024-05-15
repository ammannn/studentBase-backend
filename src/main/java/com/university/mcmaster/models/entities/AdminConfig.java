package com.university.mcmaster.models.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminConfig {
    private String id;
    private String stripeProductIdForListing;
    private Amount stripeProductAmountForListing;
}
