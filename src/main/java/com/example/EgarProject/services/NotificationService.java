package com.example.EgarProject.services;

import com.example.EgarProject.models.Notification;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ENotificationType;
import com.example.EgarProject.pojo.NotificationDTO;
import com.example.EgarProject.repos.NotificationRepo;
import com.example.EgarProject.repos.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final UserRepo userRepo;
    public NotificationService(NotificationRepo notificationRepo, UserRepo userRepo) {
        this.notificationRepo = notificationRepo;
        this.userRepo = userRepo;
    }
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String username) {
        SseEmitter emitter = new SseEmitter();
       // emitters.put(ThreadLocalRandom.current().nextLong(), emitter);
        emitters.put(username, emitter);
        // Удаление эмиттера при разрыве соединения
        emitter.onCompletion(() -> emitters.values().remove(emitter));
        emitter.onTimeout(() -> emitters.values().remove(emitter));

        return emitter;
    }
    @Transactional
    @Async
    public void sendNotification(NotificationDTO notificationDTO) {
        SseEmitter emitter = emitters.get(notificationDTO.getUsername());
        User user=userRepo.findByUsername(notificationDTO.getUsername()).get();
        saveNotification(user,notificationDTO.getMessage(),ENotificationType.MESSAGE_RECEIVED);
        if (emitter != null) {
            try {
                Map<String, Object> notificationData = new HashMap<>();
                notificationData.put("message", notificationDTO.getMessage());
                notificationData.put("timestamp", notificationDTO.getTimestamp());

                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(new ObjectMapper().writeValueAsString(notificationData)));

            } catch (IOException e) {
                // Обработка ошибок
            }
        }
    }

    public void saveNotification(User user, String message, ENotificationType type) {
        Notification notification = new Notification(user, message, type, LocalDateTime.now());
        notificationRepo.save(notification);


    }
    public List<Notification> getUserNotifications(String username){
        if(userRepo.findByUsername(username).isEmpty()){
            throw new RuntimeException("Пользователя с таким username нет");
        }

        return notificationRepo.findByUser(userRepo.findByUsername(username).get());
    }

    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepo.findByUserAndType(user, ENotificationType.MESSAGE_RECEIVED);
    }

    public void markAsRead(Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepo.findById(notificationId);
        notificationOptional.ifPresent(notification -> {
            notification.setType(ENotificationType.MESSAGE_READ);
            notificationRepo.save(notification);
        });
    }
}
