package com.university.mcmaster.utils;

import com.university.mcmaster.models.entities.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.university.mcmaster.utils.Constants.EMAIL_REGEX;

public class Utility {

    public static CustomUserDetails customUserDetails(HttpServletRequest request){
        if(null != request){
            Object temp = request.getAttribute("user");
            if(null != temp) return (CustomUserDetails) temp;
        }
        return null;
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
