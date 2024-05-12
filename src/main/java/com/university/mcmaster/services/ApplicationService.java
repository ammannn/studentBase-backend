package com.university.mcmaster.services;

import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.models.dtos.request.AddOrRemoveStudentsForApplicationRequestDto;
import com.university.mcmaster.models.dtos.request.CreateApplicationRequestDto;
import com.university.mcmaster.models.entities.Application;
import com.university.mcmaster.models.entities.Time;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ApplicationService {
    static List<ApplicationStatus> getAllowedRentalUnitStatusForOwner() {
        return Arrays.asList(
                ApplicationStatus.rejected,
                ApplicationStatus.approved,
                ApplicationStatus.visit_requested,
                ApplicationStatus.view_property,
                ApplicationStatus.review_in_process
        );
    }

    static List<ApplicationStatus> getAllowedRentalUnitStatusForStudentToListApplication() {
        return Arrays.asList(
                ApplicationStatus.visit_requested,
                ApplicationStatus.view_property,
                ApplicationStatus.pending_document_upload,
                ApplicationStatus.review_in_process,
                ApplicationStatus.approved,
                ApplicationStatus.rejected
        );
    }

    static List<ApplicationStatus> getAllowedRentalUnitStatusForStudentToTakeAction() {
        return Arrays.asList(
                ApplicationStatus.pending_document_upload,ApplicationStatus.review_in_process
        );
    }

    static List<String> getRequiredFilesForApplicationApproval() {
        return Arrays.asList(
                FilePurpose.bank_statement.toString(),
                FilePurpose.credit_score_report.toString(),
                FilePurpose.gic_certificate.toString(),
                FilePurpose.parents_bank_statement.toString(),
                FilePurpose.student_id.toString(),
                FilePurpose.national_id.toString()
        );
    }

    ResponseEntity<?> getApplications(ApplicationStatus status,String rentalUnitId,String lastSeen,int limit, String requestId, HttpServletRequest request);

    ResponseEntity<?> createApplication(CreateApplicationRequestDto requestDto, String requestId, HttpServletRequest request);

    ResponseEntity<?> updateApplication(String applicationId, String requestId, HttpServletRequest request);

    ResponseEntity<?> deleteApplication(String applicationId, String requestId, HttpServletRequest request);

    ResponseEntity<?> updateApplicationStatus(String applicationId, ApplicationStatus status, String requestId, HttpServletRequest request);

    boolean isVistingTimeSlotAvailble(String ownerId,LocalDate date, String timeZone, Time start, Time end);

    ResponseEntity<?> addOrRemoveStudentsForApplication(String applicationId, AddOrRemoveStudentsForApplicationRequestDto requestDto, String requestId, HttpServletRequest request);

    ResponseEntity<?> getApplicationById(String applicationId, String requestId, HttpServletRequest request);

    Application getApplicationById(String applicationId);

    void updateApplication(String applicationId, Map<String, Object> updateMap);

    ResponseEntity<?> updateApplicationStatusV2(List<String> applicationIds, ApplicationStatus status, String requestId, HttpServletRequest request);
}
