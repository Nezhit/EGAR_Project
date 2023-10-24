package com.example.EgarProject.controllers;

import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.User;
import com.example.EgarProject.services.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller

public class AccountController {
    @Autowired
    UserInfo userInfo;
    @GetMapping("/account")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess(HttpServletRequest request,Model model ) {
       Optional<User> user=  userInfo.getInfo( request);
        model.addAttribute("user",user);
        List<ChangeJournal> tasks= userInfo.getUserTasksByUsername(request);
        model.addAttribute("tasks",tasks);
        //System.out.println("ДЛЯ ФРОНТА! " +tasks.stream().iterator().next().getChangeTime());
        //tasks.forEach(task->task.getTask().getTaskCon().forEach(con->con.g));
        tasks.forEach(task -> System.out.println("INFORMATION "+task.getUser().getUsername()+" "+task.getTask().getDescription()+" "+task.getTask().getTaskCon()+" "+task.getChangeText()+" "+task.getChangeTime()));
        tasks.forEach(taskk->taskk.getTask().getTaskCon().forEach(taskcon -> System.out.println(taskcon.getCondition() ) ));
        return "account";
    }
    @PostMapping("/account")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String changeCon(HttpServletRequest request,Model model){
        Optional<User> user=  userInfo.getInfo( request);
        model.addAttribute("user",user);
        List<ChangeJournal> tasks= userInfo.getUserTasksByUsername(request);
        model.addAttribute("tasks",tasks);
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
