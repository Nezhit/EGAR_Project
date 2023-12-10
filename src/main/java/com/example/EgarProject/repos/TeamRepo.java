package com.example.EgarProject.repos;

import com.example.EgarProject.models.Team;
import com.example.EgarProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepo extends JpaRepository<Team,Long > {
    Optional<Team> findByName(String name);
    Optional<Team> findById(Long id);
    Optional<Team> findByTeamLead(User teamLead);
    @Query("SELECT t.members FROM Team t WHERE t.teamLead.id = :teamLeadId")
    List<User> findMembersByTeamLeadId(@Param("teamLeadId") Long teamLeadId);

}
