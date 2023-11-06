package com.example.EgarProject.pojo;

import java.time.LocalDateTime;

public class TaskCreationRequest {
    private String description;
    private LocalDateTime deadline;

    public TaskCreationRequest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
