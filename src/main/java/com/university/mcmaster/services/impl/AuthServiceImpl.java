package com.university.mcmaster.services.impl;

import com.university.mcmaster.controllers.LogInResponseDto;
import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.exceptions.*;
import com.university.mcmaster.models.dtos.response.RentalUnitOwnerLogInResponse;
import com.university.mcmaster.models.dtos.response.StudentLogInResponse;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.StudentDocFile;
import com.university.mcmaster.models.entities.User;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.LogInRequestDto;
import com.university.mcmaster.models.dtos.request.RegisterRequestDto;
import com.university.mcmaster.services.AuthService;
import com.university.mcmaster.services.UserService;
import com.university.mcmaster.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final ResponseMapper responseMapper;

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
        List<UserRole> roles = new ArrayList<>(){{
            add(UserRole.student);
            add(UserRole.user);
        }};
        if(userDetails.getEmail().equals(EnvironmentVariables.ADMIN_EMAIL)){
            roles.add(UserRole.admin);
        }
        student = User.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .role(roles)
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
            return ResponseEntity.ok(ApiResponse.builder().build());
        }
        responseDto.setRegistered(true);
        if(user.getRole().contains(UserRole.student)){
            Map<String,HashMap<String,Object>> docs = responseMapper.getStudentDocs(user);
            responseDto.setStudent(StudentLogInResponse.builder()
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .verificationStatus(user.getVerificationStatus())
                    .name(user.getName())
                    .userRole(UserRole.student)
                    .admin(user.getRole().contains(UserRole.admin))
                    .documents(docs)
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
}
