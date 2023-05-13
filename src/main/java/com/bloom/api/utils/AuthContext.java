package com.bloom.api.utils;

import com.bloom.api.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthContext {
    public static Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getEmail() {
        return getAuth().getName();
    }

    public static Integer getUserId() {
        User user = getUser();
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    public static User getUser() {
        Authentication auth = getAuth();
        if (auth != null && auth.getPrincipal() instanceof User)
            return (User) auth.getPrincipal();
        return null;
    }
}
