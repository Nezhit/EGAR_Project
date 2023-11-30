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
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Controller
public class TeamController {
    @Autowired
    UserRepo userRepo;
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }
@PostMapping("/createteam")
    public ResponseEntity<String> createTeams(@Valid @RequestBody TeamDTO teamDTO, BindingResult bindingResult){
        return teamService.createTeam(teamDTO, bindingResult);
    }
    @PostMapping("/appointleader")
    public ResponseEntity<String> appointLeader(@Valid @RequestBody TaskLeadRequest taskLeadRequest, BindingResult bindingResult){
        return teamService.appointLead(taskLeadRequest,bindingResult);
    }
    @GetMapping("/xxx")
    public ResponseEntity<String> xxx(){
        TeamDTO teamDTO= new TeamDTO();
        Set<User> users=new HashSet<>();

        userRepo.findAll().forEach(user -> users.add(user));
        teamDTO.setName("Tesss");
        teamDTO.setTeamLead(userRepo.findByUsername("user7").get());
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "teamDTO");

//
//        // вызываем метод сервиса для создания команды

        return teamService.createTeam(teamDTO, bindingResult);
    }
}
