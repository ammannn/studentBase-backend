package com.university.mcmaster.models.dtos.request;

import com.university.mcmaster.enums.Gender;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDto {
    private String email;
    private long dob;
    private String address;
    private String phoneNumber;
    private String country;
    private Gender gender;
    private String emergencyContact;
    private String name;
    private String role;
    private String authToken;
    private String nationality;
}
