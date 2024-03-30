package com.university.mcmaster.controllers;

import com.university.mcmaster.models.dtos.request.LogInRequestDto;
import com.university.mcmaster.models.dtos.request.RegisterRequestDto;
import com.university.mcmaster.services.AuthService;
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
            @RequestParam("requestId") String requestId
    ){
        return authService.registerUser(requestDto,requestId);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LogInRequestDto requestDto,
            @RequestParam("requestId") String requestId
    ){
        return authService.login(requestDto,requestId);
    }
}
