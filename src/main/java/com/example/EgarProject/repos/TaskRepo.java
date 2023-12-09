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
    @Query("SELECT t FROM Task t LEFT JOIN t.changes c WHERE c.task IS NULL OR (t.id IS NOT NULL AND c.id IS NULL)")
    List<Task> findTasksWithoutUser();
    // Поиск задач по состоянию (TaskCon)
    @Query("SELECT DISTINCT t FROM Task t JOIN t.changes c WHERE c.user = :user")
    List<Task> findTasksByUser(@Param("user") User user);
    @Query("SELECT TIMESTAMPDIFF(HOUR, CURRENT_TIMESTAMP, t.deadline) FROM Task t WHERE t.id = :taskId")
    Long getHoursUntilDeadline(@Param("taskId") Long taskId);
    // Получение задач, отсортированных по id
    @Query("SELECT t FROM Task t ORDER BY t.id ASC")
    List<Task> findTasksSortedByIdAsc();
    @Query("SELECT t FROM Task t ORDER BY t.id DESC")
    List<Task> findTasksSortedByIdDesc();
    // Получение задач, отсортированных по описанию (по алфавиту)
    @Query("SELECT t FROM Task t ORDER BY t.description")
    List<Task> findTasksSortedByDescription();

    // Получение задач, отсортированных по дате создания (по возрастанию)
    @Query("SELECT t FROM Task t ORDER BY t.created ASC")
    List<Task> findTasksSortedByCreatedAsc();
    // Получение задач, отсортированных по дате создания (по убыванию)
    @Query("SELECT t FROM Task t ORDER BY t.created DESC")
    List<Task> findTasksSortedByCreatedDsc();

    // Получение задач, отсортированных по приоритету (по убыванию)
    @Query("SELECT t FROM Task t ORDER BY t.priority DESC")
    List<Task> findTasksSortedByPriorityDesc();
    // Получение задач, отсортированных по приоритету (по возрастанию)
    @Query("SELECT t FROM Task t ORDER BY t.priority ASC")
    List<Task> findTasksSortedByPriorityAsc();

    // Получение задач, отсортированных по сложности (по возрастанию)
    @Query("SELECT t FROM Task t ORDER BY t.complexity ASC")
    List<Task> findTasksSortedByComplexityAsc();
    // Получение задач, отсортированных по сложности (по убыванию)
    @Query("SELECT t FROM Task t ORDER BY t.complexity DESC")
    List<Task> findTasksSortedByComplexityDsc();
    Optional<Task> findByTaskCons(TaskCon taskCon);

    // Поиск задач, связанных с конкретным пользователем
   // Optional<Task> findByUser(User user);

    // Поиск задач, связанных с конкретным пользователем и состоянием (TaskCon)
   // Optional<Task> findByUserAndTaskCons(User user, TaskCon taskCon);


}
