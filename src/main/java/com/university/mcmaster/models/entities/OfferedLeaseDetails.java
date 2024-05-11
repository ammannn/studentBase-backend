package com.university.mcmaster.models.entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferedLeaseDetails {
    private String fileId;
    private String filePath;
    private long offeredOn;
}
