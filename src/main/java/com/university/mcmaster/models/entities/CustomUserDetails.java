package com.university.mcmaster.models.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private String[] roles;
    private String email;
    private boolean registered;
    private String id;

    public CustomUserDetails(String id, String[] roles,String email,boolean registered){
        this.roles = roles;
        this.email = email;
        this.registered = registered;
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null != this.roles ? Arrays.stream(this.roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.registered;
    }

    public String[] getRoles() {
        return roles;
    }

    public CustomUserDetails setRoles(String[] roles) {
        this.roles = roles;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CustomUserDetails setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isRegistered() {
        return registered;
    }

    public CustomUserDetails setRegistered(boolean registered) {
        this.registered = registered;
        return this;
    }

    public String getId() {
        return id;
    }

    public CustomUserDetails setId(String id) {
        this.id = id;
        return this;
    }
}
