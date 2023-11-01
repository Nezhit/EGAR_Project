package com.example.EgarProject.services;

import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ERole;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class HRService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TaskRepo taskRepo;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    public Optional<User> HRFindEmployee() {
        /*List<User> users = userRepo.findAll();
        users.stream().iterator().next().getRoles().stream().forEach(r-> System.out.println("ROLES= "+r.getName()));
        List<User> filteredUsers=users.stream()
                .filter(user -> user.getRoles().stream()
                        .noneMatch(role -> role.getName().equals(ERole.ROLE_MODERATOR) || role.getName().equals(ERole.ROLE_ADMIN)))
                .collect(Collectors.toList());
        return filteredUsers;*/
        List<ERole> excludedRoles = Arrays.asList(ERole.ROLE_MODERATOR, ERole.ROLE_ADMIN);
        return userRepo.findEmployeesWithoutRoles(excludedRoles);
    }
    @Scheduled(cron = "0 0 * * * *") // Запускать каждый час
    public void checkTaskDeadlines() {
        // Optional<Task>tasks =taskRepo.findTasksForDeadline();
        LocalDateTime now = LocalDateTime.now();
        List<Task> overdueTasks = taskRepo.findOverdueTasks(now);
        List<Task> tasksWithEndedLaterThanDeadline = taskRepo.findTasksWithEndedLaterThanDeadline();
        List<Task> tasksWithTwoDaysDifference = taskRepo.findTasksWithTwoDaysDifference(now.plusDays(2));
        overdueTasks.forEach(task -> {
            System.out.println("overdueTask ID: " + task.getId());
            System.out.println("overdueTask Name: " + task.getDescription());
            // Выведите другие поля задачи, если они есть
            System.out.println("----------------------");
        });
        tasksWithEndedLaterThanDeadline.forEach(task -> {
            System.out.println("tasksWithEndedLaterThanDeadline ID: " + task.getId());
            System.out.println("tasksWithEndedLaterThanDeadline Name: " + task.getDescription());
            // Выведите другие поля задачи, если они есть
            System.out.println("----------------------");
        });
        tasksWithTwoDaysDifference.forEach(task -> {
            System.out.println("tasksWithTwoDaysDifference ID: " + task.getId());
            System.out.println("tasksWithTwoDaysDifference Name: " + task.getDescription());
            // Выведите другие поля задачи, если они есть
            System.out.println("----------------------");
        });

        List<List<Task>> notifications = new ArrayList<>();
        notifications.add(overdueTasks);
        notifications.add(tasksWithEndedLaterThanDeadline);
        notifications.add(tasksWithTwoDaysDifference);
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data(notifications));
            } catch (IOException e) {
                // Обработка ошибок, если таковые возникнут при отправке уведомлений
            }
        }
    }

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

}
