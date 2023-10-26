package com.example.EgarProject.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "task_conditions", // Имя промежуточной таблицы
            joinColumns = @JoinColumn(name = "task_id"), // Имя столбца с внешним ключом в таблице task_conditions
            inverseJoinColumns = @JoinColumn(name = "condition_id") // Имя столбца с внешним ключом в таблице task_conditions
    )
    private Set<TaskCon> taskCons = new HashSet<>();
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<ChangeJournal> changes = new HashSet<>();
    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
    }

    public Task() {
    }

    public Task(String description, LocalDateTime created, LocalDateTime ended) {
        this.description = description;
        this.created = created;
        this.ended = ended;

    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<TaskCon> getTaskCon() {
        return taskCons;
    }

    public void setTaskCon(Set<TaskCon> taskCon) {
        this.taskCons = taskCon;
    }

    public Set<TaskCon> getTaskCons() {
        return taskCons;
    }

    public Set<ChangeJournal> getChanges() {
        return changes;
    }

    public Long getId() {
        return id;
    }
}