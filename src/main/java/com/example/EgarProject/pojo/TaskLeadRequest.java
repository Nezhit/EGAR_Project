package com.example.EgarProject.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class TaskLeadRequest extends SignupRequest {
    private String teamName;

    public String getTeamName() {
        return teamName;
    }

    public void setTaskName(String taskName) {
        this.teamName = taskName;
    }

    @Override
    public String toString() {
        return "TaskLeadRequest{" +
                super.toString()+
                "teamName='" + teamName + '\'' +
                '}';
    }
}
