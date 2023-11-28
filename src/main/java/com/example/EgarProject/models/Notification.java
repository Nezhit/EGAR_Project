package com.example.EgarProject.models;

import com.example.EgarProject.models.enums.ENotificationType;
import jakarta.persistence.*;


import java.time.LocalDateTime;
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "message")
    private String message;

    @Enumerated(EnumType.STRING)
    private ENotificationType type;

    private LocalDateTime timestamp;

    public Notification(User user, String message, ENotificationType type, LocalDateTime timestamp) {
        this.user = user;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ENotificationType getType() {
        return type;
    }

    public void setType(ENotificationType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

// геттеры, сеттеры и конструкторы
}

