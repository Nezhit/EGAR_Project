package com.example.EgarProject.services;

import com.example.EgarProject.models.Task;
import com.example.EgarProject.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    TaskRepo taskRepo;
    public List<Task> findTaskWithoutUser() {
        List<Task> tasksWithoutUser = taskRepo.findTasksWithoutUser();
        return tasksWithoutUser;
    }

}
