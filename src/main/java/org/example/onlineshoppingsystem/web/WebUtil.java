package org.example.onlineshoppingsystem.web;

import org.example.onlineshoppingsystem.auth.JwtUser;
import org.springframework.security.core.Authentication;

final class WebUtil {
    private WebUtil() {}
    static Long currentUserId(Authentication auth) {
        var p = auth.getPrincipal();
        if (p instanceof JwtUser u) return u.getId();
        throw new IllegalStateException("No authenticated user");
    }

    static boolean isAdmin(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        var p = auth.getPrincipal();
        if (p instanceof JwtUser u) {
            return "ADMIN".equalsIgnoreCase(u.getRole());
        }

        return false;
    }
}