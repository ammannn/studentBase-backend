package com.university.mcmaster.services.impl;

import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.User;
import com.university.mcmaster.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepo.findUserByEmail(username);
        System.out.println("calling user details service");
        return new CustomUserDetails(null,null,null,false);
    }
}
