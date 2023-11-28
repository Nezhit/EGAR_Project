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
    private Set<User> members = new HashSet<>();
    private User teamLead;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public User getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(User teamLead) {
        this.teamLead = teamLead;
    }
}
