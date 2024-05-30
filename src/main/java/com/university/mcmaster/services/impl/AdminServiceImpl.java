package com.university.mcmaster.services.impl;

import com.google.firebase.auth.UserRecord;
import com.university.mcmaster.enums.*;
import com.university.mcmaster.exceptions.EntityNotFoundException;
import com.university.mcmaster.exceptions.InvalidParamValueException;
import com.university.mcmaster.exceptions.MissingRequiredParamException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.integrations.sheerid.model.SheerIdVerificationDetails;
import com.university.mcmaster.integrations.stripe.service.StripePaymentService;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.response.MethodResponse;
import com.university.mcmaster.models.entities.*;
import com.university.mcmaster.repositories.AdminRepo;
import com.university.mcmaster.repositories.UserRepo;
import com.university.mcmaster.repositories.VerificationRepo;
import com.university.mcmaster.services.AdminService;
import com.university.mcmaster.services.RentalUnitService;
import com.university.mcmaster.services.UserService;
import com.university.mcmaster.utils.Constants;
import com.university.mcmaster.utils.EnvironmentVariables;
import com.university.mcmaster.utils.FirebaseAuthenticationService;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserService userService;
    private final RentalUnitService rentalUnitService;
    private final UserRepo userRepo;
    private final AdminRepo adminRepo;
    private final VerificationRepo verificationRepo;

    @EventListener
    public void createAdminUser(ApplicationStartedEvent event){
        log.trace("****************   env vars  ******************");
        log.trace("GOOGLE_APPLICATION_CREDENTIALS : "  +EnvironmentVariables.GOOGLE_APPLICATION_CREDENTIALS);
        log.trace("BUCKET_NAME : "  +EnvironmentVariables.BUCKET_NAME);
        log.trace("PROJECT_ID : "  +EnvironmentVariables.PROJECT_ID);
        log.trace("ADMIN_EMAIL : "  +EnvironmentVariables.ADMIN_EMAIL);
        log.trace("******************************************");
        String email = EnvironmentVariables.ADMIN_EMAIL;
        if(null != email && false == email.isEmpty()){
            log.trace("checking for admin account for email : " + email);
            User user = userRepo.findUserByEmail(email);
            if(null == user){
                log.trace("user admin not account found, creating new admin account for : " + email);
                UserRecord record = FirebaseAuthenticationService.createAdminAccount(email);
                if(null != record){
                    user = User.builder()
                            .createdOn(Instant.now().toEpochMilli())
                            .id(record.getUid())
                            .dashboard(Dashboard.builder().build())
                            .email(email.trim().toLowerCase())
                            .name("mcmaster admin")
                            .verificationStatus(VerificationStatus.verified)
                            .role(Arrays.asList(UserRole.user,UserRole.admin))
                            .build();
                    userRepo.save(user);
                    User finalUser1 = user;
                    FirebaseAuthenticationService.updateClaims(user.getId(),new HashMap<String, Object>(){{
                        put("roles", finalUser1.getRole().stream().map(UserRole::toString).collect(Collectors.joining(",")));
                        put("verified", true);
                    }});
                }else{
                    log.trace("failed to create admin account");
                }
            }else if(false == user.getRole().contains(UserRole.admin)) {
                log.trace("found an existing admin account with missing admin role, updating user");
                User finalUser = user;
                user.getRole().add(UserRole.admin);
                userRepo.update(user.getId(),new HashMap<String, Object>(){{
                    put("role",finalUser.getRole());
                }});
                FirebaseAuthenticationService.updateClaims(user.getId(),new HashMap<String, Object>(){{
                    put("roles", finalUser.getRole().stream().map(UserRole::toString).collect(Collectors.joining(",")));
                    put("verified", true);
                }});
            }
        }else{
            log.trace("no admin email set");
        }
        AdminConfig adminConfig = adminRepo.getAdminConfig();
        if(null == adminConfig){
            log.trace("no admin config doc found, creating new");
            adminConfig = AdminConfig.builder()
                    .id(Constants.ADMIN_CONFIG_DOC_ID)
                    .build();
            adminRepo.saveAdminConfig(adminConfig);
        }
        if(null == adminConfig.getStripeProductIdForListing()) {
            log.trace("no stripe product found for listing payment, creating one");
            MethodResponse<StripeProduct,?,?> res = StripePaymentService.createProductAndPrice("landlord_listing_plan","this product represent price required to make a listing live by land lord", Currency.cad,100*100);
            if(false == res.isFlag()){
                log.trace("created stripe product for listing payment");
                adminRepo.updateAdminConfig(new HashMap<String,Object>(){{
                    put("stripeProductIdForListing",res.getResult_1().getStripeProductId());
                    put("stripeProductAmountForListing",Amount.builder()
                            .currency(Currency.cad)
                            .amount(100)
                            .build());
                }});
            }else{
                log.trace("failed to create stripe product for listing payment");
            }
        }
    }

    @Override
    public ResponseEntity<?> getUsers(VerificationStatus verificationStatus,int limit,String lastSeen, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        log.trace("request received to get users by admin [requestId : "+requestId+"]");
        if(null == userDetails || (false == userDetails.getRoles().contains(UserRole.admin) && false == userDetails.getRoles().contains(UserRole.platform_admin))) throw new UnAuthenticatedUserException();
        List<User> users = userService.getPaginatedUsersByVerificationStatusForAdmin(verificationStatus,limit,lastSeen);
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(users)
                        .status(200)
                .build());
    }

    @Override
    public ResponseEntity<?> getRentalUnits(VerificationStatus verificationStatus,int limit,String lastSeen, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || (false == userDetails.getRoles().contains(UserRole.admin) && false == userDetails.getRoles().contains(UserRole.platform_admin))) throw new UnAuthenticatedUserException();
        List<RentalUnit> rentalUnits = rentalUnitService.getPaginatedRentalUnitsByVerificationStatusAndDeletedFalseForAdmin(verificationStatus,limit,lastSeen);
        return ResponseEntity.ok(ApiResponse.builder()
                .data(rentalUnits)
                .status(200)
                .build());
    }

    @Override
    public ResponseEntity<?> updateRentalUnitsStatus(String rentalUnitId, VerificationStatus verificationStatus, String reason, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || (false == userDetails.getRoles().contains(UserRole.admin) && false == userDetails.getRoles().contains(UserRole.platform_admin))) throw new UnAuthenticatedUserException();
        if(null == rentalUnitId || rentalUnitId.trim().isEmpty()) throw new MissingRequiredParamException();
        RentalUnit rentalUnit = rentalUnitService.findRentalUnitById(rentalUnitId);
        if(null == rentalUnit) throw new EntityNotFoundException();
        rentalUnitService.updateRentalUnit(rentalUnitId,new HashMap<String, Object>(){{
            put("verificationStatus",verificationStatus);
            if(VerificationStatus.failed == rentalUnit.getVerificationStatus()) put("eligibleForListing",false);
            else put("eligibleForListing", PaymentStatus.successful == rentalUnit.getPaymentStatus());
            put("reason",reason);
        }});
        return ResponseEntity.ok(ApiResponse.builder()
                        .status(200)
                        .msg("updated status")
                .build());
    }

    @Override
    public ResponseEntity<?> updateUserStatus(String userId, VerificationStatus verificationStatus, String reason, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || (false == userDetails.getRoles().contains(UserRole.admin) && false == userDetails.getRoles().contains(UserRole.platform_admin))) throw new UnAuthenticatedUserException();
        if(null == userId || userId.trim().isEmpty()) throw new MissingRequiredParamException();
        User user = userService.findUserById(userId);
        if(null == user) throw new EntityNotFoundException();
        userService.updateUser(userId,new HashMap<String, Object>(){{
            put("verificationStatus",verificationStatus);
            put("reason",reason);
        }});
        return ResponseEntity.ok(ApiResponse.builder()
                .status(200)
                .msg("updated status")
                .build());
    }

    @Override
    public ResponseEntity<?> addStudentSheerIdData(SheerIdVerificationDetails details, String requestId, HttpServletRequest request) {
        if(
                null == details ||
                        null == details.getPersonInfo() ||
                        false == Utility.isStrValuePresent(details.getPersonInfo().getEmail()) ||
                        false == Utility.isStrValuePresent(details.getPersonInfo().getFirstName()) ||
                        false == Utility.isStrValuePresent(details.getPersonInfo().getLastName()) ||
                        false == Utility.isStrValuePresent(details.getPersonInfo().getBirthDate()) ||
                        null == details.getPersonInfo().getOrganization() ||
                        false == Utility.isStrValuePresent(details.getPersonInfo().getOrganization().getName()) ||
                        false == Utility.isStrValuePresent(details.getPersonInfo().getOrganization().getCountry())
        ){
            throw new InvalidParamValueException();
        }
        details.setId(UUID.randomUUID().toString());
        details.setCreated(Instant.now().toEpochMilli());
        details.setUpdated(Instant.now().toEpochMilli());
        verificationRepo.save(details);
        return ResponseEntity.ok("saved details");
    }
}
