package com.example.EgarProject.services;

import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ERole;
import com.example.EgarProject.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HRService {
    @Autowired
    private UserRepo userRepo;

    public Optional<User> HRFindEmployee() {
        /*List<User> users = userRepo.findAll();
        users.stream().iterator().next().getRoles().stream().forEach(r-> System.out.println("ROLES= "+r.getName()));
        List<User> filteredUsers=users.stream()
                .filter(user -> user.getRoles().stream()
                        .noneMatch(role -> role.getName().equals(ERole.ROLE_MODERATOR) || role.getName().equals(ERole.ROLE_ADMIN)))
                .collect(Collectors.toList());
        return filteredUsers;*/
        List<ERole> excludedRoles = Arrays.asList(ERole.ROLE_MODERATOR, ERole.ROLE_ADMIN);
        return userRepo.findEmployeesWithoutRoles(excludedRoles);
    }
}
