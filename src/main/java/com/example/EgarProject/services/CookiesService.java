package com.example.EgarProject.services;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookiesService {
    public String extractUsername(jakarta.servlet.http.HttpServletRequest request){
        String headerAuth = request.getHeader("username");
        final jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        String username = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
                if ("username".equals(cookie.getName())) {

                    username = cookie.getValue();

                }
            }
        }
        return username;
    }
}
