package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalUnitOwnerLogInResponse{
    private String email;
    private String phoneNumber;
    private UserRole userRole;
    private String name;
    private boolean admin;
}
