package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.models.entities.OfferedLeaseDetails;
import com.university.mcmaster.models.entities.RequestedVisitingSchedule;
import com.university.mcmaster.models.entities.SignedLeaseDetails;
import com.university.mcmaster.models.entities.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationForRentalUnitOwner {
    private String applicationId;
    private List<StudentForOwner> students;
    private RentalUnitForOwner rentalUnit;
    private StudentForOwner createdBy;
    private ApplicationStatus applicationStatus;
    private long createdOn;
    private long lastUpdatedOn;
    private RequestedVisitingSchedule visitingSchedule;
    private OfferedLeaseDetails offeredLeaseDetails;
    private SignedLeaseDetails signedLeaseDetails;
}
