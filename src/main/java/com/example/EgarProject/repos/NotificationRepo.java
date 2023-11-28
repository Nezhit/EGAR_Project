package com.example.EgarProject.repos;

import com.example.EgarProject.models.Notification;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ENotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {
    Optional<Notification> findById(Long id);
    List<Notification> findByUserAndType(User user, ENotificationType type);
}
