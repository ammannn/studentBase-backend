package com.university.mcmaster.utils;

import com.university.mcmaster.models.entities.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
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

    public static <T> boolean areListsEqual(List<T> list1, List<T> list2) {
        if (list1 == null && list2 == null) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            T element1 = list1.get(i);
            T element2 = list2.get(i);
            if (false == element1.equals(element2)) {
                return false;
            }
        }
        return true;
    }
}
