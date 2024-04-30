package com.university.mcmaster.controllers;

import com.university.mcmaster.models.dtos.request.UpdateUserRequestDto;
import com.university.mcmaster.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(
            @RequestBody UpdateUserRequestDto requestDto,
            HttpServletRequest request,
            @RequestHeader("requestId") String requestId
    ){
        return userService.updateUser(requestDto,requestId,request);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUserDetails(
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return userService.getUserDetails(requestId,request);
    }

    @GetMapping("/users-for-applications")
    public ResponseEntity<?> searchUserForApplication(
            @RequestBody String email,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return userService.searchUserForApplication(email,requestId,request);
    }
}
