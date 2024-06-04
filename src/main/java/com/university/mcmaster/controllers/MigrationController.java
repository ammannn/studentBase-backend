package com.university.mcmaster.controllers;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.storage.*;
import com.google.common.collect.ImmutableList;
import com.google.firebase.cloud.FirestoreClient;
import com.stripe.model.tax.Registration;
import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.entities.Application;
import com.university.mcmaster.models.entities.Dashboard;
import com.university.mcmaster.models.entities.StudentDocFile;
import com.university.mcmaster.models.entities.User;
import com.university.mcmaster.repositories.*;
import com.university.mcmaster.utils.EnvironmentVariables;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/migration")
public class MigrationController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private LikeAndRatingRepo likeAndRatingRepo;
    @Autowired
    private RentalUnitRepo rentalUnitRepo;
    @Autowired
    private ApplicationRepo applicationRepo;
    @Autowired
    private PaymentRepo paymentRepo;
    @Autowired
    private CalendarRepo calendarRepo;
    @Autowired
    private FileRepo fileRepo;

    @GetMapping("/user-doc")
    public ResponseEntity<?> fixUserDocsMap() {
        try {
            for (QueryDocumentSnapshot document : FirestoreClient.getFirestore().collection(FirestoreConstants.FS_USERS)
                    .get().get().getDocuments()) {
                FirestoreClient.getFirestore().collection(FirestoreConstants.FS_USERS)
                        .document(document.getId())
                        .update(new HashMap<String, Object>() {{
                            for (FilePurpose filePurpose : FilePurpose.validForStudent()) {
                                put("documentPaths." + filePurpose.toString(), StudentDocFile.builder().build());
                            }
                        }});
            }
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.ok(e.getMessage());
        }
        return ResponseEntity.ok("updated docs map for users");
    }

    @GetMapping("/storage-cors")
    public ResponseEntity<?> fixStorageCors() {
        Storage storage = StorageOptions.newBuilder().setProjectId(EnvironmentVariables.PROJECT_ID).build().getService();
        Bucket bucket = storage.get(EnvironmentVariables.BUCKET_NAME);
        List<HttpMethod> methods = new ArrayList<HttpMethod>() {{
            add(HttpMethod.GET);
            add(HttpMethod.PUT);
            add(HttpMethod.POST);
            add(HttpMethod.DELETE);
        }};
        Cors corsConfiguration = Cors.newBuilder()
                .setOrigins(ImmutableList.of(Cors.Origin.of("*")))
                .setMethods(ImmutableList.copyOf(methods))
                .setResponseHeaders(ImmutableList.of("Content-Type"))
                .setMaxAgeSeconds(3600)
                .build();
        bucket.toBuilder().setCors(ImmutableList.of(corsConfiguration)).build().update();
        return ResponseEntity.ok("updated cors");
    }

    @GetMapping("/student-verification-status")
    public ResponseEntity<?> studentVerificationStatus(){
        List<User> users = userRepo.getAllUsersByRole(UserRole.student);
        for (User user : users) {
            if(null == user.getVerificationStatus()){
                userRepo.update(user.getId(),new HashMap<String, Object>(){{
                    put("verificationStatus", VerificationStatus.pending);
                }});
            }
        }
        System.out.println("done");
        return ResponseEntity.ok("updated users");
    }

    @GetMapping("/landlord-dashboard")
    public ResponseEntity<?> updateLandlordDashboard(){
        List<User> users = userRepo.getAllUsersByRole(UserRole.rental_unit_owner);
        for (User user : users) {
            userRepo.update(user.getId(),new HashMap<String, Object>(){{
                put("dashboard", Dashboard.builder().build());
            }});
        }
        return ResponseEntity.ok("updated user's dashboard");
    }

    @GetMapping("/leas-term-to-string")
    public ResponseEntity<?> leasTermToString(){
        try {
            for (QueryDocumentSnapshot document : FirestoreClient.getFirestore().collection(FirestoreConstants.FS_RENTAL_UNITS)
                    .get().get().getDocuments()) {
                FirestoreClient.getFirestore().collection(FirestoreConstants.FS_RENTAL_UNITS)
                        .document(document.getId())
                        .update("leaseTerm","1_year");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("updated field");
    }

    @GetMapping("/index-check")
    public ResponseEntity<?> indexApi(){
        userRepo.findUserByEmail("test");
        userRepo.getAllUsersByRole(UserRole.admin);
        userRepo.getPaginatedUsersByVerificationStatusForAdmin(VerificationStatus.failed,10,"test");
        userRepo.getPaginatedUsersByVerificationStatusForAdmin(VerificationStatus.failed,0,null);
        userRepo.getPaginatedUsersByVerificationStatusForAdmin(VerificationStatus.failed,10,null);
        userRepo.getPaginatedUsersByVerificationStatusForAdmin(VerificationStatus.failed,0,"test");
        likeAndRatingRepo.getLikeAndRatingDocsByUserIdAndDeletedFalse("test");
        likeAndRatingRepo.findByUserIdAndRentalUnitIdAndDeletedFalse("test","test");
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test","test","test",100l,100l,"test",100,"test");
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(null,"test","test","test",100l,100l,"test",100,"test");
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test",null,"test",100l,100l,"test",100,"test");
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test","test",null,100l,100l,"test",100,"test");
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test","test","test",0,100l,"test",100,"test");
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test","test","test",100l,0,"test",100,"test");
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test","test","test",100l,100l,"test",100,"test");
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test","test","test",100l,100l,"test",100,"test");
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test",null,null,100l,100l,"test",100,null);
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test","test","test",100l,100l,"test",100,null);
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test","test","test",100l,100l,"test",100,"test");
        rentalUnitRepo.getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(List.of("test"),"test",null,null,0,0,"test",100,"test");
        rentalUnitRepo.getRentalUnitByUserIdAndDeletedFalse("test",10,"test");
        rentalUnitRepo.getRentalUnitByUserIdAndDeletedFalse("test",0,null);
        rentalUnitRepo.getRentalUnitByUserIdAndDeletedFalse("test",0,"test");
        rentalUnitRepo.getRentalUnitByUserIdAndDeletedFalse("test",10,null);
        rentalUnitRepo.getRentalUnitByUserIdAndDeletedFalseAndEligibilityForListing("test",true,10,null);
        rentalUnitRepo.getRentalUnitByUserIdAndDeletedFalseAndEligibilityForListing("test",false,10,null);
        rentalUnitRepo.getRentalUnitByUserIdAndDeletedFalseAndEligibilityForListing("test",false,10,"null");
        rentalUnitRepo.getRentalUnitByUserIdAndDeletedFalseAndEligibilityForListing("test",false,0,"null");
        applicationRepo.getApplicationByStudentIdAndRentalUnitIdAndDeletedFalse("test","test");
        applicationRepo.getApplicationByNullableRentalUnitIdAndStatusAndDeletedFalse("test",ApplicationStatus.approved);
        applicationRepo.getApplicationByNullableRentalUnitIdAndStatusAndDeletedFalse(null,ApplicationStatus.approved);
        applicationRepo.getPaginatedApplicationsByStudentIdAndNullableRentalUnitIdAndStatusAndDeletedFalse("null","test",ApplicationStatus.approved,10,"test");
        applicationRepo.getPaginatedApplicationsByStudentIdAndNullableRentalUnitIdAndStatusAndDeletedFalse("null","test",ApplicationStatus.approved,10,null);
        applicationRepo.getPaginatedApplicationsByStudentIdAndNullableRentalUnitIdAndStatusAndDeletedFalse("null","test",ApplicationStatus.approved,0,null);
        applicationRepo.getPaginatedApplicationsByStudentIdAndNullableRentalUnitIdAndStatusAndDeletedFalse("null","test",null,0,null);
        applicationRepo.getPaginatedApplicationsByStudentIdAndNullableRentalUnitIdAndStatusAndDeletedFalse("null",null,ApplicationStatus.approved,0,null);
        applicationRepo.getPaginatedApplicationsByStudentIdAndNullableRentalUnitIdAndStatusAndDeletedFalse("null",null,null,0,null);
        fileRepo.getFilesByRentalUnitIdAndDeletedFalseAndUploadedOnGcpTrue("test");
        fileRepo.getFilesByRentalUnitIdAndUploadedOnGcpTrueAndDeletedFalse("test");
        fileRepo.getFilesByUserIdAndPurposeLeaseDocAndDeletedFalse("test");
        return ResponseEntity.ok("");
    }
}
