package com.example.EgarProject.controllers;

import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ETaskCon;
import com.example.EgarProject.pojo.TaskCreationRequest;
import com.example.EgarProject.repos.TaskConRepo;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.HRService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller

public class HRController {
    @Autowired
    HRService hrService;

    @Autowired
    TaskRepo taskRepo;
    @Autowired
    TaskConRepo taskConRepo;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    @Async
    @GetMapping("/hrpanel")
    @PreAuthorize(" hasRole('MODERATOR') ")
    public String HRtaskPanel(Model model){
        //List<User> users = hrService.HRFindEmployee();
        //hrService.checkTaskDeadlines();
        List<User> users=hrService.HRFindEmployee();
        model.addAttribute("users", users);

        return "HRpanel";
    }


    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter();

        // Добавьте эмиттер в список активных эмиттеров
        emitters.add(emitter);

        emitter.onCompletion(() -> {
            // Удалите эмиттер из списка по завершении соединения
            emitters.remove(emitter);
        });

        return emitter;

    }
    @PostMapping("/send-task-notifications")
    public ResponseEntity<String> sendTaskNotifications() {
        sendSSEUpdate();
        return ResponseEntity.ok("Task notifications sent successfully");
    }



    public void sendSSEUpdate() {
        List<List<Task>> notifications = hrService.checkTaskDeadlines();
        for (SseEmitter emitter : emitters) {
            try {
                // Отправьте обновленные данные на клиентский эмиттер
                emitter.send(notifications);
            } catch (IOException e) {
                // Обработайте ошибки отправки данных, если необходимо
                emitter.complete(); // Завершите соединение в случае ошибки
            }
        }
    }

    @GetMapping("/main")
    public String showMainPanel(Authentication authentication, Model model) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_MODERATOR"))) {
            // Логика для модератора (панель создания задачи)
            return "createTaskPanel";
        } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            // Логика для пользователя (список задач)
            // Получите список задач, которые пользователь может видеть и передайте его в модель
            // ...
            // model.addAttribute("tasks", tasks);
            return "main";
        } else {
            // Логика для других ролей или неавторизованных пользователей
            return "accessDenied";
        }
    }
    @PostMapping("/create-task")
    @Transactional
    @PreAuthorize(" hasRole('MODERATOR') ")
    public ResponseEntity<String> createTask( @RequestBody TaskCreationRequest taskCreationRequest){
        hrService.createTask(taskCreationRequest);

        return ResponseEntity.ok("Task created");
    }


}