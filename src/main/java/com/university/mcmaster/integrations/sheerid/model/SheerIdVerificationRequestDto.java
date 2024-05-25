package com.university.mcmaster.integrations.sheerid.model;

import com.university.mcmaster.models.dtos.request.SheerIdUniversityRequestDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheerIdVerificationRequestDto {
    private String email;
//    private String birthDate;
//    private String firstName;
//    private String lastName;
//    private SheerIdUniversityRequestDto organization;
//    private String phoneNumber;
}
