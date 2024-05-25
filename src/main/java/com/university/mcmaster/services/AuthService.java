package com.university.mcmaster.services;

import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.integrations.sheerid.model.SheerIdVerificationRequestDto;
import com.university.mcmaster.models.dtos.request.LogInRequestDto;
import com.university.mcmaster.models.dtos.request.RegisterRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import javax.naming.ServiceUnavailableException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface AuthService {
    public static List<String> RoleRequests = Arrays.asList(UserRole.student.toString(),UserRole.rental_unit_owner.toString());
    ResponseEntity<?> registerUser(RegisterRequestDto requestDto, String requestId);
    ResponseEntity<?> login(LogInRequestDto requestDto,String requestId);
    ResponseEntity<?> adminLogin(LogInRequestDto requestDto, String requestId, HttpServletRequest request);
    ResponseEntity<?> verifyOnSheerId(SheerIdVerificationRequestDto requestDto,String country, String requestId);

    ResponseEntity<?> handleSheerIdVerification(Map<String,Object> requestDto, String sigHeader);
}
