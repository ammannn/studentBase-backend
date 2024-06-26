package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.VerificationStatus;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentForStudent {
    private String userId;
    private String email;
    private String phoneNumber;
    private String name;
    private String nationality;

    private VerificationStatus verificationStatus;
    private long verifiedOn;
    private Map<String,HashMap<String,Object>> docs;
    private boolean documentationCompleted;
    private String profileImageUrl;
}
