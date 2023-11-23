package com.example.EgarProject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="changes")
public class ChangeJournal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
    private Task task;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "changeTime")
    private LocalDateTime changeTime;

    @Column(name = "changeText")
    private String changeText;

    public ChangeJournal() {

    }

    public ChangeJournal(Task task, User user, LocalDateTime changeTime, String changeText) {
        this.task = task;
        this.user = user;
        this.changeTime = changeTime;
        this.changeText = changeText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(LocalDateTime changeTime) {
        this.changeTime = changeTime;
    }

    public String getChangeText() {
        return changeText;
    }
    public String getTaskDescription() {
        if (task != null) {
            return task.getDescription();
        }
        return "";
    }

    public void setChangeText(String changeText) {
        this.changeText = changeText;
    }
}
