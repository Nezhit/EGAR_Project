package com.example.EgarProject.services;

import com.example.EgarProject.models.Team;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ESpecialization;
import com.example.EgarProject.pojo.TaskLeadRequest;
import com.example.EgarProject.pojo.TeamDTO;
import com.example.EgarProject.repos.TeamRepo;
import com.example.EgarProject.repos.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;
import java.util.Set;

@Service
public class TeamService {
    private final TeamRepo teamRepo;
    private final UserRepo userRepo;

    public TeamService(TeamRepo teamRepo, UserRepo userRepo) {
        this.teamRepo = teamRepo;
        this.userRepo = userRepo;
    }
    @Transactional
    public ResponseEntity<String> createTeam(TeamDTO teamDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().body("Ошибка валидации: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        if (teamRepo.findByName(teamDTO.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Такая команда уже существует");
        }
        if (teamRepo.findByTeamLead(teamDTO.getTeamLead()).isPresent()) {
            return ResponseEntity.badRequest().body("Этот Тим Лид уже назначен, выберете не занятого");
        }
        if (!userRepo.findByUsername(teamDTO.getTeamLead().getUsername()).get().getSpecialization().equals(ESpecialization.TEAM_LEAD)) {
            return ResponseEntity.badRequest().body("Пользователь не является Тим Лидом");
        }

        Team team = new Team();
        team.setName(teamDTO.getName());
        team.setMembers(teamDTO.getMembers());
        team.setTeamLead(teamDTO.getTeamLead());
        teamRepo.save(team);
        Long teamId = team.getId();
        Set<User> members = teamDTO.getMembers();
        User teamLead = teamDTO.getTeamLead();


        // Обновляем идентификатор команды для участников
        members.forEach(member -> {
            Optional<User> existingUserOptional = userRepo.findByUsername(member.getUsername());
            if (existingUserOptional.isPresent()) {
                // Пользователь существует, обновляем только команду
                User existingUser = existingUserOptional.get();
                existingUser.setTeam(team);
                userRepo.saveAndFlush(existingUser);
            } else {
                // Создаем нового пользователя
                member.setTeam(team);
                userRepo.saveAndFlush(member);
            }
        });


        // Обновляем идентификатор команды для Тим Лида
        Optional<User> teamLeadOptional = userRepo.findByUsername(teamLead.getUsername());
        if (teamLeadOptional.isPresent()) {
            // Тим Лид существует, обновляем только команду
            User existingTeamLead = teamLeadOptional.get();
            existingTeamLead.setTeam(team);
            userRepo.saveAndFlush(existingTeamLead);
        } else {
            // Создаем нового Тим Лида
            teamLead.setTeam(team);
            userRepo.saveAndFlush(teamLead);
        }


        return ResponseEntity.ok("Команда создана");
    }
    public ResponseEntity<String> appointLead(TaskLeadRequest taskLeadRequest,BindingResult bindingResult){
        System.out.println(taskLeadRequest);
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации
            return ResponseEntity.badRequest().body("Ошибка валидации: " + bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        User user;
        Team team;
        if(userRepo.findByUsername(taskLeadRequest.getUsername()).isPresent()){
             user=userRepo.findByUsername(taskLeadRequest.getUsername()).get();
            if (!user.getSpecialization().equals(ESpecialization.TEAM_LEAD)) {
                return ResponseEntity.badRequest().body("Пользователь не является Тим Лидом");
            }
            if(teamRepo.findByName(taskLeadRequest.getTeamName()).isEmpty()){
                return ResponseEntity.badRequest().body("Команды с таким названием не существует");
            }else if(teamRepo.findByName(taskLeadRequest.getTeamName()).get().getTeamLead().equals(user)){
                return ResponseEntity.badRequest().body("У этой команды уже этот Тим Лид");

            }
            team=teamRepo.findByName(taskLeadRequest.getTeamName()).get();
            team.setTeamLead(user);
            teamRepo.saveAndFlush(team);
            return ResponseEntity.ok("Лидер назначен");
        }else{
            return ResponseEntity.badRequest().body("Тим лида с таким ником не существует");
        }
    }
}
