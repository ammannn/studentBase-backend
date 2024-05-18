package com.university.mcmaster.integrations.sheerid.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheerIdVerificationRequestDto {
    private String email;
    private String birthDate;
    private String firstName;
    private String lastName;
    private SheerIdUniversity organization;
    private String phoneNumber;
}
