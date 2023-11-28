package com.example.EgarProject.repos;

import com.example.EgarProject.models.Team;
import com.example.EgarProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepo extends JpaRepository<Team,Long > {
    Optional<Team> findByName(String name);
    Optional<Team> findById(Long id);
    Optional<Team> findByTeamLead(User teamLead);
}
