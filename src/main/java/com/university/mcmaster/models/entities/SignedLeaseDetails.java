package com.university.mcmaster.models.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignedLeaseDetails {
    private String fileId;
    private String filePath;
    private long signedOn;
}
