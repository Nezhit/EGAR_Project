package com.example.EgarProject.repos;

import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT u.id FROM User u JOIN u.roles r WHERE r.name IN (:excludedRoles))")
    List<User> findEmployeesWithoutRoles(@Param("excludedRoles") List<ERole> excludedRoles);
    List<User> findAll();
    Boolean existsByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.team IS NULL")
    List<User> findUsersWithoutTeam();
    Boolean existsByEmail(String email);

}
