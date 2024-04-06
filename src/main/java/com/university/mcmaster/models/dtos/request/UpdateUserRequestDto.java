package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UpdateUserRequestDto {
    private VerificationStatus verificationStatus;
    private String email;
    private String phoneNumber;
    private String name;
    private String dob;
    private String nationality;
    private String emergencyContact;
    private String additionalEmail;
    private List<Address> addresses;
}
