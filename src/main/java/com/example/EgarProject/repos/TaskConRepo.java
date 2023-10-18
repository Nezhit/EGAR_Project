package com.example.EgarProject.repos;

import com.example.EgarProject.models.TaskCon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskConRepo extends JpaRepository<TaskCon,Long> {
}
