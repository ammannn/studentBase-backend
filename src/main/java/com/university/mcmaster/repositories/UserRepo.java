package com.university.mcmaster.repositories;

import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.entities.User;

import java.util.List;
import java.util.Map;

public interface UserRepo {
    User findUserByEmail(String username);
    User findById(String id);
    boolean save(User user);
    void update(String userId,Map<String, Object> updateMap);
    List<User> getPaginatedUsersByVerificationStatusForAdmin(VerificationStatus verificationStatus, int limit, String lastSeen);

    List<User> getAllUsersByRole(UserRole userRole);
}
