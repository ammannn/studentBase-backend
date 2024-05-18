package com.university.mcmaster.integrations.sheerid.model;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheetIdVerificationResponseDto {
    private String verificationId;
    private String currentStep;
    private List<String> errorIds;
    private String segment;
    private String subSegment;
    private String locale;
    private String country;
    private long created;
    private long updated;
    private String redirectUrl;
    private String submissionUrl;
}
