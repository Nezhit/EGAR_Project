package com.example.EgarProject.repos;

import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.enums.ETaskCon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskConRepo extends JpaRepository<TaskCon,Long> {
    // Поиск состояния (TaskCon) по его идентификатору
    Optional<TaskCon> findById(Long id);

    // Поиск состояния (TaskCon) по его значению (ETaskCon)
    Optional<TaskCon> findByCondition(ETaskCon condition);
    @Modifying
    @Query("INSERT INTO TaskCon (condition) VALUES (:value1)")
    void saveTaskCon(@Param("value1") ETaskCon value1);
}
