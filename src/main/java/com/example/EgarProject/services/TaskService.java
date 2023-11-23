package com.example.EgarProject.services;

import com.example.EgarProject.models.Task;
import com.example.EgarProject.repos.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepo  taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<Task> findTaskWithoutUser() {
        List<Task> tasksWithoutUser = taskRepo.findTasksWithoutUser();
        return tasksWithoutUser;
    }


}
