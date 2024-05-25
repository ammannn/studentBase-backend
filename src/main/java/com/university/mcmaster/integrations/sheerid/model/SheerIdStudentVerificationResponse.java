package com.university.mcmaster.integrations.sheerid.model;

import lombok.*;

import java.util.List;
import java.util.Map;

/*
"verificationId": "111111111111111111111111",
"currentStep": "success",
"rewardCode": "MY_CODE",
"errorIds": [ ],
"segment": "student",
"subSegment": null,
"locale": "en-US"
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheerIdStudentVerificationResponse {
    private String verificationId;
    private String currentStep;
    private String rewardCode;
    private String segment;
    private String subSegment;
    private String locale;
    private List<String> errorIds;
    private Map<String,Object> rewardData;
}
