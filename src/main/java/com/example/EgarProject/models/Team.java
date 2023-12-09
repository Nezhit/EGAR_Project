package com.example.EgarProject.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teams",uniqueConstraints = {@UniqueConstraint(columnNames="name")})
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @OneToMany(mappedBy = "team")
    private Set<Task> tasks = new HashSet<>();
    @OneToMany(mappedBy = "team")
    private Set<User> members = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "team_lead_id")
    private User teamLead;
    public boolean addUser(User user){
        if(this.members.contains(user)){
            return false;
        }
        this.members.add(user);
        return true;
    }

    public Team() {
    }

    public Team(String name, Set<User> members, User teamLead,Set<Task> tasks) {
        this.name = name;
        this.members = members;
        this.teamLead = teamLead;
        this.tasks=tasks;
    }

    public Team(String name, User teamLead) {
        this.name = name;
        this.teamLead = teamLead;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getId() {
        return id;
    }
}