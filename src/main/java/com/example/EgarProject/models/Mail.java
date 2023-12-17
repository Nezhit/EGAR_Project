package com.example.EgarProject.models;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
@Entity
@Table(name = "emails")
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender")
    private String sender;
    @Column(name = "mailto")
    private String receiver;
    @Column(name = "subject")
    private String subject;
    @Column(name = "message")
    private String message;
    //private List<Object> attachments;
    //private Map<String, Object> props;


    public Mail() {
    }

    public Mail(String sender , String receiver, String subject,String message) {
        this.receiver = receiver;
        this.sender = sender;
        this.message=message;
        this.subject=subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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