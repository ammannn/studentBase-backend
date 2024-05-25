package com.university.mcmaster.services.impl;

import com.university.mcmaster.controllers.LogInResponseDto;
import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.exceptions.*;
import com.university.mcmaster.integrations.sheerid.SheerIdService;
import com.university.mcmaster.integrations.sheerid.model.*;
import com.university.mcmaster.models.dtos.response.RentalUnitOwnerLogInResponse;
import com.university.mcmaster.models.dtos.response.StudentLogInResponse;
import com.university.mcmaster.models.entities.*;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.LogInRequestDto;
import com.university.mcmaster.models.dtos.request.RegisterRequestDto;
import com.university.mcmaster.repositories.VerificationRepo;
import com.university.mcmaster.services.AuthService;
import com.university.mcmaster.services.UserService;
import com.university.mcmaster.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.naming.ServiceUnavailableException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final ResponseMapper responseMapper;
    private final VerificationRepo verificationRepo;

    @Override
    public ResponseEntity<?> registerUser(RegisterRequestDto requestDto, String requestId) {
        verifyRegisterUserRequest(requestDto);
        User user = null;
        if(UserRole.student.toString().equals(requestDto.getRole())){
            user = registerStudent(requestDto,requestId);
        } else if(UserRole.rental_unit_owner.toString().equals(requestDto.getRole())){
            user = registerRentalUnitOwner(requestDto,requestId);
        }
        if(null != user) {
            User finalUser = user;
            FirebaseAuthenticationService.updateClaims(user.getId(),new HashMap<String, Object>(){{
                put("roles", finalUser.getRole().stream().map(UserRole::toString).collect(Collectors.joining(",")));
                put("verified",false);
            }});
            return ResponseEntity.ok(ApiResponse.builder()
                            .status(200)
                            .msg("user registered")
                    .build());
        }
        throw new ActionNotAllowedException("register_user","invalid user role request",400);
    }

    private User registerRentalUnitOwner(RegisterRequestDto requestDto, String requestId) {
        CustomUserDetails userDetails = FirebaseAuthenticationService.verifyToken(requestDto.getAuthToken());
        if(null == userDetails) throw new InvalidParamValueException("authToken");
        User rentalUnitOwner = userService.findUserById(userDetails.getId());
        if(null != rentalUnitOwner) throw new ActionNotAllowedException("register_user","user already exists",400);
        List<UserRole> roles = new ArrayList<>(){{
            add(UserRole.rental_unit_owner);
            add(UserRole.user);
        }};
        if(userDetails.getEmail().equals(EnvironmentVariables.ADMIN_EMAIL)){
            roles.add(UserRole.admin);
        }
        rentalUnitOwner = User.builder()
                .id(userDetails.getId())
                .email(requestDto.getEmail())
                .nationality(requestDto.getNationality())
                .dashboard(Dashboard.builder().build())
                .phoneNumber(requestDto.getPhoneNumber())
                .role(roles)
                .createdOn(Instant.now().toEpochMilli())
                .build();
        boolean isSaved = userService.saveUser(rentalUnitOwner);
        if(isSaved){
            return rentalUnitOwner;
        }
        throw new FailedToPerformOperation();
    }

    private User registerStudent(RegisterRequestDto requestDto, String requestId) {
        CustomUserDetails userDetails = FirebaseAuthenticationService.verifyToken(requestDto.getAuthToken());
        if(null == userDetails) throw new InvalidParamValueException("authToken");
        User student = userService.findUserById(userDetails.getId());
        if(null != student) throw new ActionNotAllowedException("register_user","user already exists",400);
        SheerIdVerificationDetails verificationData = verificationRepo.getLatestSheerIdVerificationDetailsByEmail(userDetails.getEmail());
        if(null == verificationData || null == verificationData.getPersonInfo().getOrganization()) throw new ActionNotAllowedException("register_user","user is not verified on sheer id");
        List<UserRole> roles = new ArrayList<>(){{
            add(UserRole.student);
            add(UserRole.user);
        }};
        if(userDetails.getEmail().equals(EnvironmentVariables.ADMIN_EMAIL)){
            roles.add(UserRole.admin);
        }
        student = User.builder()
                .id(userDetails.getId())
                .nationality(verificationData.getPersonInfo().getOrganization().getCountry())
                .email(userDetails.getEmail())
                .name(verificationData.getPersonInfo().getFirstName() + " " + verificationData.getPersonInfo().getLastName())
                .phoneNumber(verificationData.getPersonInfo().getPhoneNumber())
                .dashboard(Dashboard.builder().build())
                .dob(verificationData.getPersonInfo().getBirthDate())
                .organizationName(verificationData.getPersonInfo().getOrganization().getName())
                .sheerIdOrganizationId(verificationData.getPersonInfo().getOrganization().getIdExtended())
                .role(roles)
                .verificationStatus(VerificationStatus.pending)
                .createdOn(Instant.now().toEpochMilli())
                .documentPaths(new HashMap<String,StudentDocFile>(){{
                    put(FilePurpose.bank_statement.toString(), StudentDocFile.builder().build());
                    put(FilePurpose.credit_score_report.toString(),StudentDocFile.builder().build());
                    put(FilePurpose.gic_certificate.toString(),StudentDocFile.builder().build());
                    put(FilePurpose.parents_bank_statement.toString(),StudentDocFile.builder().build());
                    put(FilePurpose.student_id.toString(),StudentDocFile.builder().build());
                    put(FilePurpose.national_id.toString(),StudentDocFile.builder().build());
                }})
                .build();
        boolean isSaved = userService.saveUser(student);
        if(isSaved){
            return student;
        }
        throw new FailedToPerformOperation();
    }

    private void verifyRegisterUserRequest(RegisterRequestDto requestDto) {
        if(false == AuthService.RoleRequests.contains(requestDto.getRole())) throw new InvalidParamValueException("role",AuthService.RoleRequests.toString());
        if(false == StringUtils.hasText(requestDto.getEmail())) throw new MissingRequiredParamException("email");
        if(false == StringUtils.hasText(requestDto.getAuthToken())) throw new MissingRequiredParamException("authToken");
//        if(false == StringUtils.hasText(requestDto.getAddress())) throw new MissingRequiredParamException("address");
//        if(false == StringUtils.hasText(requestDto.getEmergencyContact())) throw new MissingRequiredParamException("emergencyContact");
//        if(false == StringUtils.hasText(requestDto.getName())) throw new MissingRequiredParamException("name");
    }

    @Override
    public ResponseEntity<?> login(LogInRequestDto requestDto,String requestId) {
        if(null == requestDto.getAuthToken() || requestDto.getAuthToken().trim().isEmpty()) throw new MissingRequiredParamException();
        CustomUserDetails userDetails = FirebaseAuthenticationService.verifyToken(requestDto.getAuthToken());
        if(null == userDetails) throw new InvalidParamValueException("auth_token");
        User user = userService.findUserById(userDetails.getId());
        LogInResponseDto responseDto = new LogInResponseDto();
        if(null == user) {
            return ResponseEntity.ok(ApiResponse.builder()
                            .data(responseDto)
                    .build());
        }
        responseDto.setRegistered(true);
        if(user.getRole().contains(UserRole.student)){
            responseDto.setStudent(StudentLogInResponse.builder()
                    .email(user.getEmail())
                    .userId(user.getId())
                    .organizationName(user.getOrganizationName())
                    .organizationId(user.getSheerIdOrganizationId())
                    .phoneNumber(user.getPhoneNumber())
                    .verificationStatus(user.getVerificationStatus())
                    .name(user.getName())
                    .nationality(user.getNationality())
                    .userRole(UserRole.student)
                    .profileImageUrl((null != user.getProfileImage() && null != user.getProfileImage().getPath() && false == user.getProfileImage().getPath().trim().isEmpty()) ? GcpStorageUtil.createGetUrl(user.getProfileImage().getPath()).toString() : "")
                    .admin(user.getRole().contains(UserRole.admin))
                    .documents(responseMapper.getStudentDocs(user).getResult_1())
                    .verifiedOn(user.getVerifiedOn())
                    .dob(user.getDob())
                    .nationality(user.getNationality())
                    .emergencyContact(user.getEmergencyContact())
                    .additionalEmail(user.getAdditionalEmail())
                    .addresses(user.getAddresses())
                    .reason(user.getReason())
                    .build());
        }else if(user.getRole().contains(UserRole.rental_unit_owner)){
            responseDto.setRentalUnitOwner(RentalUnitOwnerLogInResponse.builder()
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .name(user.getName())
                    .admin(user.getRole().contains(UserRole.admin))
                    .userRole(UserRole.rental_unit_owner)
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(responseDto)
                        .status(200)
                .build());
    }

    @Override
    public ResponseEntity<?> adminLogin(LogInRequestDto requestDto, String requestId, HttpServletRequest request) {
        if(null == requestDto.getAuthToken() || requestDto.getAuthToken().trim().isEmpty()) throw new UnAuthenticatedUserException() ;
        CustomUserDetails userDetails = FirebaseAuthenticationService.verifyToken(requestDto.getAuthToken());
        if(null == userDetails || null == userDetails.getRoles() || false == userDetails.getRoles().contains(UserRole.admin)) throw new InvalidParamValueException("auth_token");
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(new HashMap<String,Object>(){{
                            put("email",userDetails.getEmail());
                            put("name","user_name");
                            put("admin",true);
                        }})
                .build());
    }

    @Override
    public ResponseEntity<?> verifyOnSheerId(SheerIdVerificationRequestDto requestDto,String country, String requestId) {
        if(false == Utility.isStrValuePresent(requestDto.getEmail()) || false == Utility.isValidEmail(requestDto.getEmail())) throw new MissingRequiredParamException("email");
        SheerIdVerificationDetails details = verificationRepo.getLatestSheerIdVerificationDetailsByEmail(requestDto.getEmail());
        if(null == details) throw new EntityNotFoundException();
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(new HashMap<String,Object>(){{
                            put("organization",details.getPersonInfo().getOrganization());
                            put("name",details.getPersonInfo().getFirstName() + " " + details.getPersonInfo().getLastName());
                        }})
                .build());
    }

    @Override
    public ResponseEntity<?> handleSheerIdVerification(Map<String,Object> requestDto, String sigHeader) {
        String verificationId = getVerificationId(requestDto);
        if(null != verificationId){
            SheerIdVerificationDetails details = SheerIdService.getSheerIdVerificationDetailsById(verificationId);
            if(null != details){
                if(null != details.getPersonInfo() && null != details.getPersonInfo().getOrganization()){
                    SheerIdUniversity sheerIdUniversity = SheerIdService.getSheerIdUniversityByVerificationId(verificationId);
                    if(null != sheerIdUniversity){
                        details.getPersonInfo().setOrganization(sheerIdUniversity);
                    }
                }
                verificationRepo.save(details);
            }
        }
        return ResponseEntity.ok("ok");
    }

    private String getVerificationId(Map<String,Object> requestDto) {
        if(null !=  requestDto && requestDto.containsKey("verificationId")){
            String temp = (String) requestDto.get("verificationId");
            if(null != temp && false == temp.trim().isEmpty()) return temp.trim();
        }
        return null;
    }
}
