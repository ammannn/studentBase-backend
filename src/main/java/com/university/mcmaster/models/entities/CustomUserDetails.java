package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails implements UserDetails {

    private HashSet<UserRole> roles;
    private String email;
    private boolean verified;
    private String id;

    public CustomUserDetails(String id, UserRole[] roles,String email,boolean verified){
        this.roles = new HashSet<>();
        if(null != roles)this.roles.addAll(Arrays.asList(roles));
        this.email = email;
        this.verified = verified;
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null != this.roles ? this.roles.stream().map(r->new SimpleGrantedAuthority(r.toString())).collect(Collectors.toList()) : Collections.emptyList();
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
        return this.verified;
    }
}
