package com.university.mcmaster.controllers;

import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.models.dtos.request.AddOrRemoveStudentsForApplicationRequestDto;
import com.university.mcmaster.models.dtos.request.CreateApplicationRequestDto;
import com.university.mcmaster.models.dtos.request.UpdateApplicationRequestDto;
import com.university.mcmaster.services.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/applications")
    @Operation(
            summary = "getApplications",
            description = "api to list application by status \n" +
                    "allowed status for landlord -> \n" +
                    "rejected, approved ,visit_requested (to see applications where visit was requested) ,review_in_process (to see applications where landlord has to review applications to finally approve or reject) \n\n" +
                    "allowed status for students -> \n" +
                    "visit_requested - to see the application where students are still expecting visit to be approved \n" +
                    "view_property - to see application where visit was approved \n" +
                    "pending_document_upload - to application where document upload is pending \n" +
                    "review_in_process \n" +
                    "approved \n" +
                    "rejected"
    )
    public ResponseEntity<?> getApplications(
            @RequestParam(name = "status") ApplicationStatus status,
            @RequestParam(name = "rentalUnitId",required = false) String rentalUnitId,
            @RequestParam(name = "lastSeen",required = false) String lastSeen,
            @RequestParam(name = "limit",required = false,defaultValue = "15") int limit,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.getApplications(status,rentalUnitId,lastSeen,limit,requestId,request);
    }

    @PostMapping("/applications")
    @Operation(summary = "createApplication",description = "create application for student, timezone and data are required to create application as application will be created with status='visit_requested' which will be listed in rental unit owner for approval of visit")
    public ResponseEntity<?> createApplication(
            @RequestBody CreateApplicationRequestDto requestDto,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.createApplication(requestDto,requestId,request);
    }

    @PutMapping("/applications/{applicationId}")
    @Operation(summary = "getApplicationById")
    public ResponseEntity<?> getApplicationById(
            @PathVariable("applicationId") String applicationId,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.getApplicationById(applicationId,requestId,request);
    }

    @PutMapping("/applications/{applicationId}/status")
    @Operation(
            summary = "updateApplicationStatus",
            description = "the method is used to update status (transition application from one stage to another for example from 'pending_document_upload' to 'review_in_process' to 'approved'). following is the possible transitions \n\n" +
                    "visit_requested -> view_property \n" +
                    "view_property -> pending_document_upload \n" +
                    "pending_document_upload -> review_in_process \n" +
                    "review_in_process -> approved , rejected \n\n" +
                    "students operation -> \n\n" +
                    "from view_property to 'pending_document_upload' i.e. student can pick status='pending_document_upload' as next status if current status is view_property, (if user have viewed property next this to do is to upload docs so status 'pending_document_upload' represents status where user will upload document) \n\n" +
                    "from pending_document_upload to 'review_in_process' i.e if student's document are already uploaded then user can push application to review_in_process status, this is where applications will be listed on rental unit owner \n\n" +
                    "landlord's operation -> \n\n" +
                    "from review_in_process to approved , as landlord will have application in status 'review_in_process' where ll can review and either accept or reject application \n\n" +
                    "from review_in_process to rejected"
    )
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable("applicationId") String applicationId,
            @RequestParam("status") ApplicationStatus status,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.updateApplicationStatus(applicationId,status,requestId,request);
    }

    @PutMapping("/application/{applicationId}")
    public ResponseEntity<?> updateApplication(
            @RequestBody UpdateApplicationRequestDto requestDto,
            @PathVariable("applicationId") String applicationId,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.updateApplication(applicationId,requestDto,requestId,request);
    }

    @PutMapping("/applications/status/v2")
    public ResponseEntity<?> updateApplicationStatusV2(
            @RequestBody List<String> applicationIds,
            @RequestParam("status") ApplicationStatus status,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.updateApplicationStatusV2(applicationIds,status,requestId,request);
    }

    @DeleteMapping("/applications/{applicationId}")
    @Operation(summary = "deleteApplication")
    public ResponseEntity<?> deleteApplication(
            @PathVariable("applicationId") String applicationId,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.deleteApplication(applicationId,requestId,request);
    }

    @PutMapping("/applications/{applicationId}/students")
    @Operation(summary = "addOrRemoveStudentsForApplication",
            description = "provide userIds,\n ,new students can be added when application in the status 'pending_document_upload' \n , this api will replace current list of users with the new requested list of user ids"
    )
    public ResponseEntity<?> addOrRemoveStudentsForApplication(
            @PathVariable("applicationId") String applicationId,
            @RequestBody AddOrRemoveStudentsForApplicationRequestDto requestDto,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return applicationService.addOrRemoveStudentsForApplication(applicationId,requestDto,requestId,request);
    }
}
