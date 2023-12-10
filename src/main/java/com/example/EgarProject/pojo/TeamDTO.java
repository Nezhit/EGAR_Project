package com.example.EgarProject.pojo;

import com.example.EgarProject.models.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


import java.util.HashSet;
import java.util.Set;

public class TeamDTO {

    @NotBlank(message = "Название команды должно быть не пустое")
    private String name;
    @NotEmpty(message = "Команда должна содержать хотя бы одного участника")
    private Set<Long> members = new HashSet<>();
    private Long teamLead;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Long> getMembers() {
        return members;
    }

    public void setMembers(Set<Long> members) {
        this.members = members;
    }

    public Long getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(Long teamLead) {
        this.teamLead = teamLead;
    }
}
