package com.example.EgarProject.services;

import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.User;
import com.example.EgarProject.pojo.ReplaceUserRequest;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.repos.UserRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepo  taskRepo;
    private final UserRepo userRepo;

    public TaskService(TaskRepo taskRepo, UserRepo userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    public List<Task> findTaskWithoutUser() {
        List<Task> tasksWithoutUser = taskRepo.findTasksWithoutUser();
        return tasksWithoutUser;
    }
    @Scheduled(fixedRate = 30000)
    public void updatePriority(){
        List<Task> allTasks = taskRepo.findAll();

        for (Task task : allTasks) {
            Long hoursLeft = taskRepo.getHoursUntilDeadline(task.getId());
            int importancePoints = 0;

            if (hoursLeft != null) {
                if (hoursLeft > 0) {
                    importancePoints = 100 - (int) (hoursLeft * 2);
                    importancePoints += task.getPriority();
                    task.setPriority(importancePoints);
                } else {
                    task.setPriority(0);
                }

                taskRepo.save(task);
            }
        }
    }
    public List<Task> getOrderedByType(String type){
        List<Task> tasks=new ArrayList<>();
        switch (type){
            case "id":
                tasks=taskRepo.findTasksSortedByComplexityAsc();
                break;
            case "id2":
                tasks=taskRepo.findTasksSortedByComplexityDsc();
                break;
            case "created":
                tasks=taskRepo.findTasksSortedByCreatedAsc();
                break;
            case "created2":
                tasks=taskRepo.findTasksSortedByCreatedDsc();
                break;
            case "description":
                tasks=taskRepo.findTasksSortedByDescription();
                break;
            case "priority":
                tasks=taskRepo.findTasksSortedByPriorityAsc();
                break;
            case "priority2":
                tasks=taskRepo.findTasksSortedByPriorityDesc();
                break;
            default:
                tasks=taskRepo.findTasksWithoutUser();

        }
        return tasks;
    }
    public List<Task> getOrderedByTypeWithoutUser(String type){
        List<Task> tasks=getOrderedByType(type);
        return tasks.stream()
                .filter(task -> task.getUser() == null)
                .collect(Collectors.toList());
    }
    public boolean replaceUser(ReplaceUserRequest replaceUserRequest){
        User user= userRepo.findByUsername(replaceUserRequest.getUsername()).orElseThrow(()->{
            return new UsernameNotFoundException("User с именем "+replaceUserRequest.getUsername()+"не найден");
        });
        Task task=taskRepo.findById(replaceUserRequest.getTaskId()).orElseThrow(()->{
            return new UsernameNotFoundException("Task с id "+replaceUserRequest.getTaskId()+"не найдена");
        });
        task.setUser(user);
        taskRepo.save(task);
        return true;
    }

}
