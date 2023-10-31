package com.example.EgarProject.controllers;

import com.example.EgarProject.models.User;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.HRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller

public class HRController {
    @Autowired
    HRService hrService;
    @GetMapping("/hrpanel")
    @PreAuthorize(" hasRole('MODERATOR') ")
    public String HRtaskPanel(Model model){
        //List<User> users = hrService.HRFindEmployee();
        Optional<User> users=hrService.HRFindEmployee();
        model.addAttribute("users", users);
        return "HRpanel";
    }
}
