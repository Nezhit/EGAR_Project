package com.example.EgarProject.services;

import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ETaskCon;
import com.example.EgarProject.repos.ChangeJournalRepo;
import com.example.EgarProject.repos.TaskConRepo;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.repos.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInfo {

    private final TaskRepo taskRepo;
    private final TaskConRepo taskConRepo;
    private final UserRepo userRepo;
    private final ChangeJournalRepo changeJournalRepo;

    @Autowired
    public UserInfo(TaskRepo taskRepository, TaskConRepo taskConRepository, UserRepo userRepository,ChangeJournalRepo changeJournalRepo ) {
        this.taskRepo = taskRepository;
        this.taskConRepo = taskConRepository;
        this.userRepo = userRepository;
        this.changeJournalRepo=changeJournalRepo;
    }

    public String extractUsername(HttpServletRequest request){// ... код для получения пользователя из куков
        String username=null;

        jakarta.servlet.http.Cookie[] cookies= request.getCookies();
        //Посмотреть если можно то изменить цикл на stream
        if( cookies!=null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("username")){
                    username=cookie.getValue();
                    break;
                }
            }
        }
        return username;
    }
    public Optional<User> getInfo(HttpServletRequest request){
        Optional<User> userOptional = null;
        String username=extractUsername(request);
        if(username!=null){
            userOptional=userRepo.findByUsername(username);

        }
        if(userOptional.isPresent()){
            System.out.println("USERDATA "+userOptional);
            userOptional.ifPresent(user->{
                System.out.println(user.getUsername()+" "+user.getPassword()+" "+user.getEmail()+" "+user.getRoles());
            });
        }

        return userRepo.findByUsername(username);
    }
    public List<ChangeJournal> getUserTasksByUsername(HttpServletRequest request) {
        String username=extractUsername(request);
        Optional<User> userOptional = null;
        // Найти пользователя по username
        String finalUsername = username;
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с именем " + finalUsername + " не найден"));
        System.out.println("User= "+user);

        // Найти все задачи для данного пользователя
        return changeJournalRepo.findByUser(user);
    }
    public Map<Long, List<ChangeJournal>> getMapUserTasksByUsername(HttpServletRequest request) {
        // Найти все задачи для данного пользователя
        List<ChangeJournal> changeJournals = getUserTasksByUsername( request);
        // Группировка по taskId
        Map<Long, List<ChangeJournal>> groupedChangeJournals = changeJournals.stream()
                .collect(Collectors.groupingBy(changeJournal -> changeJournal.getTask().getId()));

        return groupedChangeJournals;
    }
    public ResponseEntity<String> assignTaskUser(HttpServletRequest request,Long id){
        String username=extractUsername(request);
        String firstCommit="Задание выбрано";
        ChangeJournal changeJournal=new ChangeJournal(taskRepo.findById(id).get(),userRepo.findByUsername(username).get(), LocalDateTime.now(),firstCommit);
        changeJournalRepo.save(changeJournal);
        return ResponseEntity.ok("Успешно прекреплены задачи");
    }
    public double calculateCompletionPercentage(User user){
        List<Task> allUserTasks=taskRepo.findTasksByUser(user);
        allUserTasks.forEach(task -> System.out.println("Состояние= "+task.getTaskCon().stream().iterator().next().getCondition()));
        long countOfDoneTasks = allUserTasks.stream()
                .filter(task -> task.getTaskCon().stream().iterator().next().getCondition().equals(ETaskCon.DONE))
                .count();
        System.out.println("Для челика с ником "+user.getUsername()+" выполненых задач = "+countOfDoneTasks);
        return (double) allUserTasks.size() /countOfDoneTasks;

    }
    public double calculateAverageTaskCompletionTime(User user) {
        List<Task> allUserTasks = taskRepo.findTasksByUser(user);

        List<Task> finishedTasks = allUserTasks.stream()
                .filter(task -> {
                    Set<TaskCon> taskCons = task.getTaskCons();
                    return taskCons != null && !taskCons.isEmpty() &&
                            taskCons.stream().anyMatch(con -> con.getCondition().equals(ETaskCon.DONE)) &&
                            task.getEnded() != null; // Убеждаемся, что ended не является null
                })
                .toList();

        if (finishedTasks.isEmpty()) {
            return 0.0;
        }

        long totalDuration = finishedTasks.stream()
                .mapToLong(task -> Duration.between(task.getCreated(), task.getEnded()).toMillis())
                .sum();

        return (double) totalDuration / (60 * 1000 * finishedTasks.size());
    }
    public double calculateTaskChangeFrequencyPerDay(User user) {
        List<ChangeJournal> userChanges = changeJournalRepo.findByUser(user);

        if (userChanges.isEmpty()) {
            return 0.0;
        }

        // Получение временных меток всех изменений пользователя
        List<LocalDateTime> changeTimestamps = userChanges.stream()
                .map(ChangeJournal::getChangeTime)
                .sorted()
                .toList();


        if (changeTimestamps.size() <= 1) {
            return 0.0;
        }

        // Вычисление времени между первым и последним изменением пользователя
        LocalDateTime firstChange = changeTimestamps.get(0);
        LocalDateTime lastChange = changeTimestamps.get(changeTimestamps.size() - 1);
        long daysBetween = ChronoUnit.DAYS.between(firstChange, lastChange);


        return daysBetween != 0 ? (double) (changeTimestamps.size() - 1) / daysBetween : 0.0;
    }

    }
