package com.example.EgarProject.controllers;

import com.example.EgarProject.pojo.TaskLeadRequest;
import com.example.EgarProject.pojo.TeamDTO;
import com.example.EgarProject.services.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TeamController {
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
}
