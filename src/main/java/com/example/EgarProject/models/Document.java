package com.example.EgarProject.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String filePath;
    @Column(name = "reading_progress")
    private int readingProgress; // Прогресс чтения (номер страницы)
    private LocalDateTime created;
    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
    }

    public Document() {
    }

    public Document(String title, String author, String filePath) {
        this.title = title;
        this.author = author;
        this.filePath = filePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public int getReadingProgress() {
        return readingProgress;
    }

    public void setReadingProgress(int readingProgress) {
        this.readingProgress = readingProgress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
