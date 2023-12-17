package com.example.EgarProject.pojo;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MailDTO {
    @NotBlank(message = "Поле 'receiver' не должно быть пустым")
    @Email(message = "Некорректный адрес электронной почты")
    private String receiver;
    @NotBlank(message = "Поле 'subject' не должно быть пустым")
    private String subject;
    @NotBlank(message = "Поле 'message' не должно быть пустым")
    private String message;

    public MailDTO() {
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
