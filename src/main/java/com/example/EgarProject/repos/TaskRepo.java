package com.example.EgarProject.repos;

import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TaskRepo extends JpaRepository<Task,Long> {
    Optional<Task> findById(Long id);
    Optional<Task> findByDescription(String description);
    // Поиск задач, созданных после указанной даты
    Optional<Task> findByCreatedAfter(LocalDateTime date);

    // Поиск задач, завершенных до указанной даты
    Optional<Task> findByEndedBefore(LocalDateTime date);

    // Поиск задач по состоянию (TaskCon)
    Optional<Task> findByTaskCons(TaskCon taskCon);

    // Поиск задач, связанных с конкретным пользователем
   // Optional<Task> findByUser(User user);

    // Поиск задач, связанных с конкретным пользователем и состоянием (TaskCon)
   // Optional<Task> findByUserAndTaskCons(User user, TaskCon taskCon);


}
