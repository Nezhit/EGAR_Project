package com.example.EgarProject.services;

import com.example.EgarProject.controllers.HRController;
import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ERole;
import com.example.EgarProject.models.enums.ETaskCon;
import com.example.EgarProject.pojo.TaskCreationRequest;
import com.example.EgarProject.repos.TaskConRepo;
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
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class HRService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private TaskConRepo taskConRepo;

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    public List<User> HRFindEmployee() {

        List<ERole> excludedRoles = Arrays.asList(ERole.ROLE_MODERATOR, ERole.ROLE_ADMIN);
        return userRepo.findEmployeesWithoutRoles(excludedRoles);
    }
    //@Scheduled(initialDelay = 20000,fixedRate = 10000) // Запускать каждый час
    public List<List<Task>> checkTaskDeadlines() {
        // Optional<Task>tasks =taskRepo.findTasksForDeadline();
        LocalDateTime now = LocalDateTime.now();
        List<Task> overdueTasks = taskRepo.findOverdueTasks(now);
        List<Task> tasksWithEndedLaterThanDeadline = taskRepo.findTasksWithEndedLaterThanDeadline();
        List<Task> tasksWithTwoDaysDifference = taskRepo.findTasksWithTwoDaysDifference(now.plusDays(2),now);

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
        System.out.println("ТУТ ОШИБКА!!");




        return notifications;

    }
    public void createTask(TaskCreationRequest taskCreationRequest){
        Task task=new Task();
        Set<TaskCon> taskCons=new HashSet<>();
        TaskCon newTaskCon=new TaskCon(ETaskCon.TODO);
        taskCons.add(newTaskCon);
        //taskConRepo.saveTaskCon(newTaskCon.getCondition());
        taskConRepo.save(newTaskCon);
        task.setTaskCon(taskCons);
        task.setDescription(taskCreationRequest.getDescription());
        task.setDeadline(taskCreationRequest.getDeadline());
        taskRepo.save(task);

    }


    /*public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

   // @Scheduled(fixedRate = 20000) // отправка уведомлений каждые 20 секунд
    public void sendNotifications() {
        List<List<Task>> notifications = checkTaskDeadlines();
        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notifications));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }

        // Удаление закрытых эмиттеров из списка
        emitters.removeAll(deadEmitters);

    }*/


}
