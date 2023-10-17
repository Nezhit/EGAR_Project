package com.example.EgarProject.services;

import com.example.EgarProject.models.User;
import com.example.EgarProject.repos.UserRepo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
@Service
public class UserInfo {
    @Autowired
    UserRepo userRepo;


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
}
