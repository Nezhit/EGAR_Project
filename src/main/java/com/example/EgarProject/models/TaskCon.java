package com.example.EgarProject.models;


import com.example.EgarProject.models.enums.ETaskCon;
import jakarta.persistence.*;

@Entity
@Table(name="conditions")
public class TaskCon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ETaskCon condition;

    public TaskCon() {
    }
    public TaskCon(ETaskCon condition) {
        this.condition=condition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ETaskCon getCondition() {
        return condition;
    }

    public void setCondition(ETaskCon condition) {
        this.condition = condition;
    }
}
