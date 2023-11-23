package com.example.EgarProject.controllers;

import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.User;
import com.example.EgarProject.pojo.ChangeConRequest;
import com.example.EgarProject.services.ChangeConService;
import com.example.EgarProject.services.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

          return "account";
    }
    @PostMapping("/account")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String changeCon(HttpServletRequest request, Model model, @RequestBody ChangeConRequest changeConRequest){
        System.out.println(changeConRequest.getSectionId()+" "+changeConRequest.getTextInput()+" "+changeConRequest.getTaskId());
        Optional<User> user = userInfo.getInfo(request);
        model.addAttribute("user",user);
        changeConService.fixChanges(changeConRequest,user);



        Map<Long, List<ChangeJournal>> groupedTasks=userInfo.getMapUserTasksByUsername(request);
        model.addAttribute("groupedTasks",groupedTasks);
        return "account";
    }
    @GetMapping("/test")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String test() {
        return "test";
    }

}
