package com.example.EgarProject.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description")
    private String description;
    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "ended")
    private LocalDateTime ended;

    @ManyToOne
    @JoinColumn(name = "condition")
    private TaskCon taskCon;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name="task_conditions",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private User user;
    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
    }

    public Task() {
    }

    public Task(String description, LocalDateTime created, LocalDateTime ended, TaskCon taskCon) {
        this.description = description;
        this.created = created;
        this.ended = ended;
        this.taskCon = taskCon;
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

    public TaskCon getTaskCon() {
        return taskCon;
    }

    public void setTaskCon(TaskCon taskCon) {
        this.taskCon = taskCon;
    }
}