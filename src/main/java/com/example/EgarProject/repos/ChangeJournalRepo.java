package com.example.EgarProject.repos;

import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChangeJournalRepo extends JpaRepository<ChangeJournal,Long> {

    List<ChangeJournal> findByTask(Task task);

    List<ChangeJournal> findByTaskAndUser(Task task, User user);
    List<ChangeJournal> findByUser(User user);
    List<ChangeJournal> findByChangeText(String changeText);
   // List<ChangeJournal> findByChangeTime(LocalDateTime changeTime);
   //List<ChangeJournal> findByChangeText(String changeText);
}
