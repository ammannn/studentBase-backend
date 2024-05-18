package com.university.mcmaster.controllers;

import com.university.mcmaster.integrations.sheerid.model.SheerIdVerificationRequestDto;
import com.university.mcmaster.models.dtos.request.LogInRequestDto;
import com.university.mcmaster.models.dtos.request.RegisterRequestDto;
import com.university.mcmaster.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequestDto requestDto,
            @RequestHeader("requestId") String requestId
    ){
        return authService.registerUser(requestDto,requestId);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LogInRequestDto requestDto,
            @RequestHeader("requestId") String requestId
    ){
        return authService.login(requestDto,requestId);
    }

    @PostMapping("/login/admin")
    public ResponseEntity<?> adminLogin(
            @RequestBody LogInRequestDto requestDto,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return authService.adminLogin(requestDto,requestId,request);
    }

    @PostMapping("/sheerId/verify")
    public ResponseEntity<?> verifyOnSheerId(
            @RequestBody SheerIdVerificationRequestDto requestDto,
            @RequestHeader("requestId") String requestId
    ){
        return authService.verifyOnSheerId(requestDto,requestId);
    }
}
