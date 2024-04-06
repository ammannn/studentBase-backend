package com.university.mcmaster.services.impl;

import com.university.mcmaster.controllers.LogInResponseDto;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.exceptions.*;
import com.university.mcmaster.models.dtos.response.RentalUnitOwnerLogInResponse;
import com.university.mcmaster.models.dtos.response.StudentLogInResponse;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.User;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.LogInRequestDto;
import com.university.mcmaster.models.dtos.request.RegisterRequestDto;
import com.university.mcmaster.services.AuthService;
import com.university.mcmaster.services.UserService;
import com.university.mcmaster.utils.FirebaseAuthenticationService;
import com.university.mcmaster.utils.GcpStorageUtil;
import com.university.mcmaster.utils.Utility;
import lombok.RequiredArgsConstructor;
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
        }
        throw new ActionNotAllowedException("register_user","invalid user role request");
    }

    private User registerRentalUnitOwner(RegisterRequestDto requestDto, String requestId) {
        CustomUserDetails userDetails = FirebaseAuthenticationService.verifyToken(requestDto.getAuthToken());
        if(null == userDetails) throw new InvalidParamValueException("authToken");
        User rentalUnitOwner = userService.findUserById(userDetails.getId());
        if(null != rentalUnitOwner) throw new ActionNotAllowedException("register_user","user already exists");
        rentalUnitOwner = User.builder()
                .id(userDetails.getId())
                .email(requestDto.getEmail())
                .phoneNumber(requestDto.getPhoneNumber())
                .role(Arrays.asList(UserRole.rental_unit_owner,UserRole.user))
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
        if(null != student) throw new ActionNotAllowedException("register_user","user already exists");
        student = User.builder()
                .id(userDetails.getId())
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .role(Arrays.asList(UserRole.student,UserRole.user))
                .createdOn(Instant.now().toEpochMilli())
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
        if(false == StringUtils.hasText(requestDto.getName())) throw new MissingRequiredParamException("name");
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
            List<Map<String,String>> docs = Optional.ofNullable(user.getDocumentPaths()).map(Map::entrySet)
                            .stream().flatMap(Collection::stream)
                            .map(e->new HashMap<String,String>(){{
                                put(e.getKey(), GcpStorageUtil.createGetUrl(e.getValue()).toString());
                            }}).collect(Collectors.toList());
            responseDto.setStudent(StudentLogInResponse.builder()
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .verificationStatus(user.getVerificationStatus())
                    .name(user.getName())
                    .userRole(UserRole.student)
                    .documents(docs)
                    .build());
        }else if(user.getRole().contains(UserRole.rental_unit_owner)){
            responseDto.setRentalUnitOwner(RentalUnitOwnerLogInResponse.builder()
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .name(user.getName())
                    .userRole(UserRole.rental_unit_owner)
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(responseDto)
                        .status(200)
                .build());
    }
}
