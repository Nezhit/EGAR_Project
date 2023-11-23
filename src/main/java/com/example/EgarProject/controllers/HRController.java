package com.example.EgarProject.controllers;

import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ETaskCon;
import com.example.EgarProject.pojo.ChangedTasksDTO;
import com.example.EgarProject.pojo.ReplaceUserRequest;
import com.example.EgarProject.pojo.TaskCreationRequest;
import com.example.EgarProject.repos.TaskConRepo;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.ChangeConService;
import com.example.EgarProject.services.HRService;
import com.example.EgarProject.services.TaskService;
import com.example.EgarProject.services.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@Validated
public class HRController {
    private final HRService hrService;
    private final TaskService taskService;
    private final UserInfo userInfo;
    private final UserRepo userRepo;
    private final ChangeConService changeConService;
    private final TaskRepo taskRepo;
    private final TaskConRepo taskConRepo;


    public HRController(HRService hrService, TaskService taskService, UserInfo userInfo,
                        UserRepo userRepo, ChangeConService changeConService,
                        TaskRepo taskRepo, TaskConRepo taskConRepo) {
        this.hrService = hrService;
        this.taskService = taskService;
        this.userInfo = userInfo;
        this.userRepo = userRepo;
        this.changeConService = changeConService;
        this.taskRepo = taskRepo;
        this.taskConRepo = taskConRepo;
    }
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    @Async
    @GetMapping("/hrpanel")
    @PreAuthorize(" hasRole('MODERATOR') ")
    public String HRtaskPanel(Model model){

        List<User> users=hrService.HRFindEmployee();
        model.addAttribute("users", users);


        return "HRpanel";
    }
    @GetMapping("/hrpanel/testtask")
    @PreAuthorize(" hasRole('MODERATOR') ")
    public ResponseEntity<List<Task>> testtask(Model model){

        model.addAttribute("tasks",hrService.getTasksWithChanges());
        return ResponseEntity.ok( hrService.getTasksWithChanges());
    }
    @GetMapping("/hrpanel/testtask/{id}")
    @PreAuthorize(" hasRole('MODERATOR') ")
    public ResponseEntity<List<ChangedTasksDTO>> changeJournalWithTaskId(Model model, @PathVariable("id") Long id){


        return ResponseEntity.ok(changeConService.getTaskChangesAndUsers(id));
    }
    @PatchMapping ("/hrpanel/replace")
    @PreAuthorize(" hasRole('MODERATOR') ")
    public ResponseEntity<String> replaceUserToTask(@RequestBody ReplaceUserRequest replaceUserRequest){


        return changeConService.replaceUser(replaceUserRequest);
    }
    @GetMapping("/tasks")
    @PreAuthorize(" hasRole('MODERATOR') ")
    public String searchtasks(){
        return "tasks";
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter();

        // Добавьте эмиттер в список активных эмиттеров
        emitters.add(emitter);

        emitter.onCompletion(() -> {
            // Удалите эмиттер из списка по завершении соединения
            System.out.println("ПРОИЗОШЕЛ КРИНЖ");
            emitters.remove(emitter);
        });

        return emitter;

    }

    @PostMapping("/send-task-notifications")
    public ResponseEntity<String> sendTaskNotifications() {
        sendSSEUpdate();
        return ResponseEntity.ok("Task notifications sent successfully");
    }

    @Transactional
    @Scheduled(fixedRate = 10000) // Отправлять каждые 10 секунд
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
            catch(IllegalStateException e){
                emitter.complete();
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

            List<Task>freeTasks= taskService.findTaskWithoutUser() ;
            model.addAttribute("freeTasks",freeTasks);

            return "main";
        } else {
            // Логика для других ролей или неавторизованных пользователей
            return "accessDenied";
        }
    }
    @PostMapping("/assign-task")
    public ResponseEntity<String> assignTask(@RequestBody ArrayList<Long> ids, HttpServletRequest request){
       ids.forEach((id) -> userInfo.assignTaskUser(request,id));



        return ResponseEntity.ok("Задача успешно присоединина");
    }
    @PostMapping("/create-task")
    @Transactional
    @PreAuthorize(" hasRole('MODERATOR') ")
    public ResponseEntity<String> createTask(@Valid @RequestBody TaskCreationRequest taskCreationRequest){
        hrService.createTask(taskCreationRequest);

        return ResponseEntity.ok("Task created");
    }
    @GetMapping("getuserstat")
    public ResponseEntity<String> getUserStat(){

        return ResponseEntity.ok("Статистика нашлась "+
                userInfo.calculateCompletionPercentage(userRepo.findByUsername("user3").get())+
                " Среднее время выполнения задачи  "+
                userInfo.calculateAverageTaskCompletionTime(userRepo.findByUsername("user3").get()) +
                " Комитов в день " +
                userInfo.calculateTaskChangeFrequencyPerDay(userRepo.findByUsername("user3").get()));
    }

}