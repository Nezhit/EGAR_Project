package com.example.EgarProject.pojo;

public class ChangeConRequest {
    private String sectionId;
    private String taskId;
    private String textInput;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public Long getTaskId() {
        return Long.parseLong( taskId);
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTextInput() {
        return textInput;
    }

    public void setTextInput(String textInput) {
        this.textInput = textInput;
    }
}
