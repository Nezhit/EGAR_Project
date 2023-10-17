package com.example.EgarProject.controllers;

import com.example.EgarProject.models.User;
import com.example.EgarProject.services.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

@Controller

public class AccountController {
    @Autowired
    UserInfo userInfo;
    @GetMapping("/account")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess(HttpServletRequest request,Model model ) {
       Optional<User> user=  userInfo.getInfo( request);
        model.addAttribute("user",user);
       // System.out.println("infa   "+userInfo.getInfo(username));
        return "account";
    }
    @GetMapping("/test")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String test() {
        return "test";
    }
}
