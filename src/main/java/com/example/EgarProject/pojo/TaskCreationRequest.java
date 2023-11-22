package com.example.EgarProject.pojo;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public class TaskCreationRequest {
    @Pattern(regexp = "^(?!\\s*$).+", message = "Описание задачи не может быть пустым или состоять только из пробелов")
    private String description;
    @FutureOrPresent(message = "Дата дедлайна должна быть в будущем или текущей")
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
