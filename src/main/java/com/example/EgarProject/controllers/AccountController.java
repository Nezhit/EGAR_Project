package com.example.EgarProject.controllers;

import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ETaskCon;
import com.example.EgarProject.pojo.ChangeConRequest;
import com.example.EgarProject.repos.ChangeJournalRepo;
import com.example.EgarProject.repos.TaskConRepo;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.services.ChangeConService;
import com.example.EgarProject.services.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.*;

@Controller

public class AccountController {
    @Autowired
    UserInfo userInfo;
    @Autowired
    ChangeConService changeConService;
    @GetMapping("/account")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess(HttpServletRequest request,Model model ) {
       Optional<User> user=  userInfo.getInfo( request);
        model.addAttribute("user",user);
        List<ChangeJournal> tasks= userInfo.getUserTasksByUsername(request);
        model.addAttribute("tasks",tasks);
        Map<Long, List<ChangeJournal>> groupedTasks=userInfo.getMapUserTasksByUsername(request);
        model.addAttribute("groupedTasks",groupedTasks);
        groupedTasks.entrySet().forEach(a-> System.out.println("VALUE="+a.getValue().stream().iterator().next().getTask().getId()));
        // Итерация по Map
        for (Map.Entry<Long, List<ChangeJournal>> entry : groupedTasks.entrySet()) {
            Long taskId = entry.getKey();
            List<ChangeJournal> changeJournals = entry.getValue();

            System.out.println("Task ID: " + taskId);

            // Итерация по списку ChangeJournal
            for (ChangeJournal changeJournal : changeJournals) {
                System.out.println("Change ID: " + changeJournal.getId());
                System.out.println("Task Description: " + changeJournal.getTaskDescription());
                System.out.println("Change Time: " + changeJournal.getChangeTime());
                System.out.println("Change Text: " + changeJournal.getChangeText());
                System.out.println("------------");
            }
        }

        //System.out.println("ДЛЯ ФРОНТА! " +tasks.stream().iterator().next().getChangeTime());
        //tasks.forEach(task->task.getTask().getTaskCon().forEach(con->con.g));
       // tasks.forEach(task -> System.out.println("INFORMATION "+task.getUser().getUsername()+" "+task.getTask().getDescription()+" "+task.getTask().getTaskCon()+" "+task.getChangeText()+" "+task.getChangeTime()));
        //tasks.forEach(taskk->taskk.getTask().getTaskCon().forEach(taskcon -> System.out.println(taskcon.getCondition() ) ));
        return "account";
    }
    @PostMapping("/account")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String changeCon(HttpServletRequest request, Model model, @RequestBody ChangeConRequest changeConRequest){
        System.out.println(changeConRequest.getSectionId()+" "+changeConRequest.getTextInput()+" "+changeConRequest.getTaskId());
        Optional<User> user = userInfo.getInfo(request);
        model.addAttribute("user",user);
        changeConService.fixChanges(changeConRequest,user);

        /*Optional<Task> oneTask = taskRepo.findById(changeConRequest.getTaskId());
        System.out.println(oneTask.stream().iterator().next().getDescription());
        if (oneTask.isPresent() && user.isPresent()) {
            Task task = oneTask.get();
            User currentUser = user.get();

            Set<TaskCon> taskCons = new HashSet<>();

            switch (changeConRequest.getSectionId()) {
                case "section1":

                    taskCons.add(taskConRepo.findByCondition(ETaskCon.TODO).get());
                    break;
                case "section2":
                    taskCons.add(taskConRepo.findByCondition(ETaskCon.IN_PROGRESS).get());
                    break;
                case "section3":
                    taskCons.add(taskConRepo.findByCondition(ETaskCon.TESTING).get());
                    break;
                case "section4":
                    taskCons.add(taskConRepo.findByCondition(ETaskCon.DONE).get());
                    break;
            }

            task.setTaskCon(taskCons);

            ChangeJournal changeJournal = new ChangeJournal(task, currentUser, LocalDateTime.now(), changeConRequest.getTextInput());

            // Сохраните объект ChangeJournal в базе данных
            changeJournalRepo.save(changeJournal);
        } else {
            // Обработка ситуации, когда задача или пользователь не найдены в базе данных
        }*/


        //changeJournalRepo.saveAndFlush(changeJournal);
        //  model.addAttribute("user",user);
       // List<ChangeJournal> tasks= userInfo.getUserTasksByUsername(request);
       // model.addAttribute("tasks",tasks);
        Map<Long, List<ChangeJournal>> groupedTasks=userInfo.getMapUserTasksByUsername(request);
        model.addAttribute("groupedTasks",groupedTasks);
        return "account";
    }
    @GetMapping("/test")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String test() {
        return "test";
    }
    @GetMapping("/main")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String main() {
        return "main";
    }
}
