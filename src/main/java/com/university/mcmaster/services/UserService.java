package com.university.mcmaster.services;

import com.university.mcmaster.models.dtos.request.UpdateUserRequestDto;
import com.university.mcmaster.models.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public interface UserService {
    User findUserById(String id);
    boolean saveUser(User user);
    ResponseEntity<?> updateUser(UpdateUserRequestDto requestDto, String requestId, HttpServletRequest request);
    void updateUser(String userId, Map<String, Object> updateMap);
}
