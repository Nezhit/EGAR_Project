package com.example.EgarProject.controllers;

import com.example.EgarProject.services.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;


    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @GetMapping("/testoviy")
    public String testNotif(){
        return "test";
    }
    @Scheduled(fixedRate = 10000)
    public void doooo(){
        notificationService.sendNotification(3L,"AAAAAA");
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribeToNotifications() {
        return notificationService.subscribe();
    }
}
