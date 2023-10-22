package com.example.EgarProject.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HRController {
    @GetMapping("/hrpanel")
    @PreAuthorize(" hasRole('MODERATOR') ")
    public String HRtaskPanel(){
        return "HRpanel";
    }
}
