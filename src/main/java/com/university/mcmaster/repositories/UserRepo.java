package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.User;

public interface UserRepo {
    User findUserByEmail(String username);

    User findById(String id);

    boolean save(User user);
}
