package com.example.EgarProject.services;

import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.User;
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


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

}
