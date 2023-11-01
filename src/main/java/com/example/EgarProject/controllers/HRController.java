package com.example.EgarProject.controllers;

import com.example.EgarProject.models.User;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.HRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller

public class HRController {
    @Autowired
    HRService hrService;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    @GetMapping("/hrpanel")
    @PreAuthorize(" hasRole('MODERATOR') ")
    public String HRtaskPanel(Model model){
        //List<User> users = hrService.HRFindEmployee();
        hrService.checkTaskDeadlines();
        Optional<User> users=hrService.HRFindEmployee();
        model.addAttribute("users", users);
        return "HRpanel";
    }


    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

    public void notifyClients(String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (Exception e) {
                // Handle exceptions if any
            }
        }
    }
}
