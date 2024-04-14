package com.university.mcmaster.services;

import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.models.dtos.request.LogInRequestDto;
import com.university.mcmaster.models.dtos.request.RegisterRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

public interface AuthService {
    public static List<String> RoleRequests = Arrays.asList(UserRole.student.toString(),UserRole.rental_unit_owner.toString());
    ResponseEntity<?> registerUser(RegisterRequestDto requestDto, String requestId);
    ResponseEntity<?> login(LogInRequestDto requestDto,String requestId);
    ResponseEntity<?> adminLogin(LogInRequestDto requestDto, String requestId, HttpServletRequest request);
}
