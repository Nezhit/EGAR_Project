package com.example.EgarProject.repos;

import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends JpaRepository<Task,Long> {
    Optional<Task> findById(Long id);
    Optional<Task> findByDescription(String description);
    // Поиск задач, созданных после указанной даты
    Optional<Task> findByCreatedAfter(LocalDateTime date);

    // Поиск задач, завершенных до указанной даты
    Optional<Task> findByEndedBefore(LocalDateTime date);
    @Query("SELECT t FROM Task t WHERE t.ended IS NULL AND t.created IS NOT NULL")
    Optional<Task> findTasksForDeadline();
    @Query("SELECT t FROM Task t WHERE t.ended IS NOT NULL AND t.created IS NOT NULL")
    Optional<Task> findTasksForDeadlineNN();
    @Query("SELECT t FROM Task t WHERE t.ended IS NULL AND t.deadline < :now")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);

    @Query("SELECT t FROM Task t WHERE t.ended IS NOT NULL AND t.ended > t.deadline")
    List<Task> findTasksWithEndedLaterThanDeadline();

    @Query("SELECT t FROM Task t WHERE t.ended IS NULL AND t.deadline < :twoDaysAfterNow AND t.deadline>:nowTime")
    List<Task> findTasksWithTwoDaysDifference(@Param("twoDaysAfterNow") LocalDateTime twoDaysAfterNow, LocalDateTime nowTime);

    // Поиск задач по состоянию (TaskCon)
    Optional<Task> findByTaskCons(TaskCon taskCon);

    // Поиск задач, связанных с конкретным пользователем
   // Optional<Task> findByUser(User user);

    // Поиск задач, связанных с конкретным пользователем и состоянием (TaskCon)
   // Optional<Task> findByUserAndTaskCons(User user, TaskCon taskCon);


}
