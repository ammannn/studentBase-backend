package com.university.mcmaster.utils;

import com.university.mcmaster.models.entities.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("running sec filter before request : " + request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        if(null != authHeader && authHeader.startsWith("Bearer ")){
            String tokenStr = authHeader.substring(7);
            CustomUserDetails userDetails = FirebaseAuthenticationService.verifyToken(tokenStr);
            if(null != userDetails){
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
                request.setAttribute("user",userDetails);
            }else{
                logger.trace("user user found for give token on firebase auth");
            }
        }else{
            logger.trace("no auth token provided");
        }
        filterChain.doFilter(request, response);
        System.out.println("running sec filter after request : "  + response.getStatus());
    }
}
