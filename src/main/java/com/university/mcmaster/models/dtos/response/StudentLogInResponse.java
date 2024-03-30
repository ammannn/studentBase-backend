package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentLogInResponse {
    private String email;
    private String phoneNumber;
    private boolean verified;
    private long verifiedOn;
    private UserRole userRole;
    private String name;
}
