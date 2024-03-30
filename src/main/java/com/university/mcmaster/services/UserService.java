package com.university.mcmaster.services;

import com.university.mcmaster.models.entities.User;

public interface UserService {
    User findUserById(String id);
    boolean saveUser(User user);
}
