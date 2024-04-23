package com.university.mcmaster.models.dtos.response;

import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.models.entities.RequestedVisitingSchedule;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationForStudent {
    private String applicationId;
    private List<StudentForStudent> students;
    private RentalUnitForStudentForListing rentalUnit;
    private StudentForStudent createdBy;
    private ApplicationStatus applicationStatus;
    private long createdOn;
    private long lastUpdatedOn;
    private RequestedVisitingSchedule visitingSchedule;
}
