package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.models.entities.Dashboard;
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
    private String occupation;
    private String preferredModOfContact;
    private String reason;
    private boolean admin;
    private String profileImageUrl;
    private String userId;
    private Dashboard dashboard;
}
