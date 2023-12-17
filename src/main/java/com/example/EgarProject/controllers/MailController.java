package com.example.EgarProject.controllers;

import com.example.EgarProject.pojo.MailDTO;
import com.example.EgarProject.services.MailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MailController {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

   @GetMapping("/sendmail")
   public String sendMailPage(){
        return "mailsender";
   }
    @PostMapping("/sendmail")
    public ResponseEntity<String> sendMail(@Valid @RequestBody MailDTO mailDTO, BindingResult bindingResult) throws MessagingException {
        //mailService.sendMail(mailDTO,bindingResult);
        return mailService.sendMail(mailDTO,bindingResult);
    }
}
