package com.university.mcmaster.services.impl;

import com.university.mcmaster.models.entities.User;
import com.university.mcmaster.repositories.UserRepo;
import com.university.mcmaster.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public User findUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public boolean saveUser(User user) {
        return userRepo.save(user);
    }
}
