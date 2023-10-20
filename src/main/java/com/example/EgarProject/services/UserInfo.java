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
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
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


    public Optional<User> getInfo(HttpServletRequest request){
        String username=null;
        Optional<User> userOptional = null;
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
        String username=null;
        Optional<User> userOptional = null;
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
        // Найти пользователя по username
        String finalUsername = username;
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с именем " + finalUsername + " не найден"));
        System.out.println("User= "+user);

        // Найти все задачи для данного пользователя
        return changeJournalRepo.findByUser(user);
    }
}
