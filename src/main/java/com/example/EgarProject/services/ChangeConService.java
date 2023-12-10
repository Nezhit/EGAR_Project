package com.example.EgarProject.services;

import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ETaskCon;
import com.example.EgarProject.pojo.ChangeConRequest;
import com.example.EgarProject.pojo.ChangedTasksDTO;
import com.example.EgarProject.pojo.ReplaceUserRequest;
import com.example.EgarProject.repos.ChangeJournalRepo;
import com.example.EgarProject.repos.TaskConRepo;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChangeConService {
    private final TaskRepo taskRepo;
    private final UserRepo userRepo;
    private final ChangeJournalRepo changeJournalRepo;
    private final HRService hrService;
    private final TaskConRepo taskConRepo;

    // Конструктор для внедрения зависимостей
    public ChangeConService(TaskRepo taskRepo, UserRepo userRepo, ChangeJournalRepo changeJournalRepo,
                            HRService hrService, TaskConRepo taskConRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.changeJournalRepo = changeJournalRepo;
        this.hrService = hrService;
        this.taskConRepo = taskConRepo;
    }
    public void fixChanges(ChangeConRequest changeConRequest,Optional<User> user){
        Optional<Task> oneTask = taskRepo.findById(changeConRequest.getTaskId());

        System.out.println(oneTask.stream().iterator().next().getDescription());
        if (oneTask.isPresent() && user.isPresent()) {
            Task task = oneTask.get();
            User currentUser = user.get();

            Set<TaskCon> taskCons = new HashSet<>();

            switch (changeConRequest.getSectionId()) {
                case "section1":

                    taskCons.add(taskConRepo.findByCondition(ETaskCon.TODO).get());
                    break;
                case "section2":
                    taskCons.add(taskConRepo.findByCondition(ETaskCon.IN_PROGRESS).get());
                    break;
                case "section3":
                    taskCons.add(taskConRepo.findByCondition(ETaskCon.TESTING).get());
                    break;
                case "section4":
                    taskCons.add(taskConRepo.findByCondition(ETaskCon.DONE).get());
                    task.setEnded(LocalDateTime.now());
                    break;
            }

            task.setTaskCon(taskCons);

            ChangeJournal changeJournal = new ChangeJournal(task, currentUser, LocalDateTime.now(), changeConRequest.getTextInput());

            // Сохраните объект ChangeJournal в базе данных
            changeJournalRepo.save(changeJournal);
        } else {
            // Обработка дописать!
        }
    }
    public List<ChangedTasksDTO> getTaskChangesAndUsers(Long id){
        List<ChangedTasksDTO> changeJournalDTOs = hrService.findLinkedChanges(id).stream()
                .map(changeJournal -> {
                    ChangedTasksDTO dto = new ChangedTasksDTO();
                    dto.setId(changeJournal.getId());
                    dto.setChangeTime(changeJournal.getChangeTime());
                    dto.setChangeText(changeJournal.getChangeText());
                    dto.setTaskDescription(changeJournal.getTaskDescription());
                    dto.setUsername(changeJournal.getUser().getUsername()); // Получаем имя пользователя
                    return dto;
                })
                .collect(Collectors.toList());
        return changeJournalDTOs;
    }
    @Transactional
    public ResponseEntity<String> replaceUser(ReplaceUserRequest replaceUserRequest) {
        Optional<User> userOptional = userRepo.findByUsername(replaceUserRequest.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<ChangeJournal> changeJournals = changeJournalRepo.findByTaskId(replaceUserRequest.getTaskId());

            // Проверяем, что задача и записи существуют
            if (!changeJournals.isEmpty()) {
                // Изменяем пользователя для каждой записи
                for (ChangeJournal changeJournal : changeJournals) {
                    changeJournal.setUser(user);
                }

                // Сохраняем изменения в базе данных
                changeJournalRepo.saveAll(changeJournals);
            } else {
                // Обработка случая, когда нет записей по задаче
                return ResponseEntity.ok("Нет записей по задаче");
            }
        } else {
            // Обработка случая, когда пользователь не найден
            throw new RuntimeException("User с именем "+replaceUserRequest.getUsername()+" не найден");

        }
        return ResponseEntity.ok("Пользователь у задачи успешно сменен");
    }
    // Метод для формирования HashMap
    public Map<String, Map<String, List<ChangeJournal>>> getChangesMapForMembers(List<User> members) {
        // Собираем изменения для каждого пользователя
        return members.stream()
                .collect(Collectors.toMap(
                        User::getUsername,
                        user -> getChangesForUser(user.getTasks())
                                .stream()
                                .collect(Collectors.groupingBy(
                                        change -> change.getTask().getDescription(),
                                        Collectors.toList()
                                ))
                ));
    }

    private List<ChangeJournal> getChangesForUser(Set<Task> tasks) {
        return tasks.stream()
                .flatMap(task -> changeJournalRepo.findByTaskId(task.getId()).stream())
                .collect(Collectors.toList());
    }


}
