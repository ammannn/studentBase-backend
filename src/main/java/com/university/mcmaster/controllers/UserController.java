package com.university.mcmaster.controllers;

import com.university.mcmaster.models.dtos.request.UpdateUserRequestDto;
import com.university.mcmaster.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "updateUser")
    @PutMapping("/users")
    public ResponseEntity<?> updateUser(
            @RequestBody UpdateUserRequestDto requestDto,
            HttpServletRequest request,
            @RequestHeader("requestId") String requestId
    ){
        return userService.updateUser(requestDto,requestId,request);
    }

    @Operation(summary = "getUserDetails")
    @GetMapping("/users")
    public ResponseEntity<?> getUserDetails(
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return userService.getUserDetails(requestId,request);
    }

    @Operation(summary = "searchUserForApplication")
    @GetMapping("/users-for-applications")
    public ResponseEntity<?> searchUserForApplication(
            @RequestBody String email,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest request
    ){
        return userService.searchUserForApplication(email,requestId,request);
    }
}
