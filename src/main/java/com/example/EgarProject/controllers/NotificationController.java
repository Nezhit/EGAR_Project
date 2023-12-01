package com.example.EgarProject.controllers;

import com.example.EgarProject.models.Notification;
import com.example.EgarProject.pojo.NotificationDTO;
import com.example.EgarProject.services.CookiesService;
import com.example.EgarProject.services.NotificationService;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Controller
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final CookiesService cookiesService;

    public NotificationController(NotificationService notificationService, CookiesService cookiesService) {
        this.notificationService = notificationService;
        this.cookiesService = cookiesService;
    }
    @GetMapping("/testoviy")
    public String testNotif(Model model,jakarta.servlet.http.HttpServletRequest request){
        String username=cookiesService.extractUsername(request);
        List<Notification> notifications=notificationService.getUserNotifications(username);
        model.addAttribute("notifications",notifications);


        return "test";
    }
//    @Scheduled(fixedRate = 10000)
//    public void doooo(){
//        notificationService.sendNotification(3L,"AAAAAA");
//    }
    @PostMapping("/sendnotif")
    public ResponseEntity<String> sendik(@RequestBody NotificationDTO notificationDTO){
        notificationService.sendNotification(notificationDTO);
        return ResponseEntity.ok("Сообщение доставлено");
    }
    @GetMapping("/subscribe")
    public SseEmitter subscribeToNotifications(jakarta.servlet.http.HttpServletRequest request) {
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

        return notificationService.subscribe(username);
    }
}
