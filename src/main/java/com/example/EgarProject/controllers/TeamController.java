package com.example.EgarProject.controllers;

import com.example.EgarProject.models.User;
import com.example.EgarProject.pojo.TaskLeadRequest;
import com.example.EgarProject.pojo.TeamDTO;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.TeamService;
import com.example.EgarProject.services.UserInfo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class TeamController {
    @Autowired
    UserRepo userRepo;
    private final UserInfo userInfo;
    private final TeamService teamService;

    public TeamController(UserInfo userInfo, TeamService teamService) {
        this.userInfo = userInfo;
        this.teamService = teamService;
    }
    @GetMapping("/createteam")
    public String getCreationTeamPage(Model model){
        List<User> usersWithoutTeam=userInfo.getUsersWithoutTeam();
        model.addAttribute("usersWithoutTeam",usersWithoutTeam);
        return "teamCreation";
    }

@PostMapping("/createteam")
    public ResponseEntity<String> createTeams(@Valid @RequestBody TeamDTO teamDTO, BindingResult bindingResult){

        return teamService.createTeam(teamDTO, bindingResult);
    }
    @PostMapping("/appointleader")
    public ResponseEntity<String> appointLeader(@Valid @RequestBody TaskLeadRequest taskLeadRequest, BindingResult bindingResult){
        return teamService.appointLead(taskLeadRequest,bindingResult);
    }

}
