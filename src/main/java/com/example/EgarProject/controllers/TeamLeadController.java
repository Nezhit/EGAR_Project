package com.example.EgarProject.controllers;

import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.User;
import com.example.EgarProject.pojo.TeamLeadMark;
import com.example.EgarProject.services.ChangeConService;
import com.example.EgarProject.services.TeamService;
import com.example.EgarProject.services.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Controller
public class TeamLeadController {
    private final TeamService teamService;
    private final ChangeConService changeConService;
    private final UserInfo userInfo;


    public TeamLeadController(TeamService teamService, ChangeConService changeConService, UserInfo userInfo) {
        this.teamService = teamService;
        this.changeConService = changeConService;
        this.userInfo = userInfo;
    }

    @GetMapping("teamleadpanel")
    public String getTeamLeadPage(Model model, HttpServletRequest request){
        User teamLead=userInfo.getInfo(request).get();
        List<User> members=teamService.getTeamMembersByLeader(teamLead.getId());
        model.addAttribute("members",members);
        Map<String, Map<String, List<ChangeJournal>>> userChanges=changeConService.getChangesMapForMembers(members);
        model.addAttribute("userChanges",userChanges);
        return "teamLeadpanel";
    }
    @PostMapping("/teamleadpanel/critisize")
    public ResponseEntity<String> critisizeChange(@RequestBody TeamLeadMark teamLeadMark, BindingResult bindingResult){


        return changeConService.critisizeChange(teamLeadMark, bindingResult);
    }
}
