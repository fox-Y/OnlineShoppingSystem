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
}