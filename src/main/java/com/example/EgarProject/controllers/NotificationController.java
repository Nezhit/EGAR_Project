package com.example.EgarProject.controllers;

import com.example.EgarProject.pojo.MessageResponse;
import com.example.EgarProject.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    //@Scheduled(fixedRate = 10000)
    public void doooo(){
        notificationService.sendNotification(3L,"AAAAAA");
    }
    @PostMapping("/sendnotif")
    public ResponseEntity<String> sendik(@RequestBody MessageResponse message){
        notificationService.sendNotification(3L,message.getMessage());
        return ResponseEntity.ok("Сообщение доставлено");
    }
    @GetMapping("/subscribe")
    public SseEmitter subscribeToNotifications() {
        return notificationService.subscribe();
    }
}
