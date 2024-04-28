package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.entities.Address;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentLogInResponse {
    private String email;
    private String phoneNumber;
    private VerificationStatus verificationStatus;
    private long verifiedOn;
    private UserRole userRole;
    private String name;
    private List<Map<String, HashMap<String,Object>>> documents;
    private boolean admin;
    private String dob;
    private String nationality;
    private String emergencyContact;
    private String additionalEmail;
    private List<Address> addresses;
    private String reason;
}
