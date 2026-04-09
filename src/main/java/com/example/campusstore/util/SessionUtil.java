package com.example.campusstore.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {

    public static final String USER_ID = "userId";
    public static final String ROLE = "role";

    public Long getCurrentUserId(HttpSession session) {
        Object userId = session.getAttribute(USER_ID);
        if (userId instanceof Long) {
            return (Long) userId;
        }
        return null;
    }

    public String getCurrentUserRole(HttpSession session) {
        Object role = session.getAttribute(ROLE);
        if (role instanceof String) {
            return (String) role;
        }
        return null;
    }

    public boolean isLoggedIn(HttpSession session) {
        return getCurrentUserId(session) != null;
    }
}