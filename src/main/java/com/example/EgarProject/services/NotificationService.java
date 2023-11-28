package com.example.EgarProject.services;

import com.example.EgarProject.models.Notification;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ENotificationType;
import com.example.EgarProject.repos.NotificationRepo;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
@Service
public class NotificationService {

    private final NotificationRepo notificationRepo;

    public NotificationService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter();
       // emitters.put(ThreadLocalRandom.current().nextLong(), emitter);
        emitters.put(3L, emitter);
        // Удаление эмиттера при разрыве соединения
        emitter.onCompletion(() -> emitters.values().remove(emitter));
        emitter.onTimeout(() -> emitters.values().remove(emitter));

        return emitter;
    }
    @Transactional
    @Async
    public void sendNotification(Long userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(message));
            } catch (IOException e) {
                // Обработка ошибок
            }
        }
    }
    public void sendNotification(User user, String message, ENotificationType type) {
        Notification notification = new Notification(user, message, type, LocalDateTime.now());
        notificationRepo.save(notification);


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
