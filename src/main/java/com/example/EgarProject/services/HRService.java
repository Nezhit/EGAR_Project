package com.example.EgarProject.services;

import com.example.EgarProject.controllers.HRController;
import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ERole;
import com.example.EgarProject.models.enums.ETaskCon;
import com.example.EgarProject.pojo.TaskCreationRequest;
import com.example.EgarProject.repos.ChangeJournalRepo;
import com.example.EgarProject.repos.TaskConRepo;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.repos.UserRepo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserRepo userRepo;
    private final TaskRepo taskRepo;
    private final TaskConRepo taskConRepo;
    private final ChangeJournalRepo changeJournalRepo;
    private final Validator validator;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Autowired
    public HRService(UserRepo userRepo, TaskRepo taskRepo, TaskConRepo taskConRepo,
                     ChangeJournalRepo changeJournalRepo, Validator validator) {
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.taskConRepo = taskConRepo;
        this.changeJournalRepo = changeJournalRepo;
        this.validator = validator;
    }

    public List<User> HRFindEmployee() {

        List<ERole> excludedRoles = Arrays.asList(ERole.ROLE_MODERATOR, ERole.ROLE_ADMIN);
        return userRepo.findEmployeesWithoutRoles(excludedRoles);
    }
    @Transactional
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
    public List<Task> getTasksWithChanges(){


        return taskRepo.findAll();
    }
    public void createTask(@Valid TaskCreationRequest taskCreationRequest){
        Task task=new Task();
        Set<TaskCon> taskCons=new HashSet<>();
        //TaskCon newTaskCon=new TaskCon(ETaskCon.TODO);
        TaskCon newTaskCon=taskConRepo.findByCondition(ETaskCon.TODO).get();
        taskCons.add(newTaskCon);

        //taskConRepo.save(newTaskCon);

        task.setTaskCon(taskCons);
        task.setDescription(taskCreationRequest.getDescription());
        task.setDeadline(taskCreationRequest.getDeadline());
        // Проверка валидации
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        if (!violations.isEmpty()) {
            // Обработка ошибок валидации (можете выбрать нужный способ)
            throw new ConstraintViolationException(violations);
        }
        taskRepo.save(task);

    }
    public List<ChangeJournal> findLinkedChanges(Long id){
        return changeJournalRepo.findByTaskId(id);
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
