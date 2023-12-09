package com.example.EgarProject.models;

import com.example.EgarProject.models.enums.EComplexity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description")
    private String description;
    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;

    @Column(name = "ended")
    private LocalDateTime ended;
    @Column(name = "deadline")
    private LocalDateTime deadline;
    @ManyToOne(optional = true)
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToMany
    @JoinTable(
            name = "task_conditions", // Имя промежуточной таблицы
            joinColumns = @JoinColumn(name = "task_id"), // Имя столбца с внешним ключом в таблице task_conditions
            inverseJoinColumns = @JoinColumn(name = "condition_id") // Имя столбца с внешним ключом в таблице task_conditions
    )
    @JsonIgnore
    private Set<TaskCon> taskCons = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<ChangeJournal> changes = new HashSet<>();
    @Column(name = "priority")
    private int priority;
    @Column(name = "complexity")
    private EComplexity complexity;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
    }

    public Task() {
    }

    public Task(String description, LocalDateTime created, LocalDateTime ended,LocalDateTime deadline) {
        this.description = description;
        this.created = created;
        this.ended = ended;
        this.deadline=deadline;
    }

    public Task(String description, LocalDateTime created, LocalDateTime ended, LocalDateTime deadline, int priority, EComplexity complexity) {
        this.description = description;
        this.created = created;
        this.ended = ended;
        this.deadline = deadline;
        this.priority = priority;
        this.complexity = complexity;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public EComplexity getComplexity() {
        return complexity;
    }

    public void setComplexity(EComplexity complexity) {
        this.complexity = complexity;
    }

    public void setTaskCons(Set<TaskCon> taskCons) {
        this.taskCons = taskCons;
    }

    public void setChanges(Set<ChangeJournal> changes) {
        this.changes = changes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task(String description, LocalDateTime created, LocalDateTime ended, LocalDateTime deadline, Team team) {
        this.description = description;
        this.created = created;
        this.ended = ended;
        this.deadline = deadline;
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getEnded() {
        return ended;
    }

    public void setEnded(LocalDateTime ended) {
        this.ended = ended;
    }

    public Set<TaskCon> getTaskCon() {
        return taskCons;
    }

    public void setTaskCon(Set<TaskCon> taskCon) {
        this.taskCons = taskCon;
    }

    public Set<TaskCon> getTaskCons() {
        return taskCons;
    }

    public Set<ChangeJournal> getChanges() {
        return changes;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }


    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Long getId() {
        return id;
    }
}