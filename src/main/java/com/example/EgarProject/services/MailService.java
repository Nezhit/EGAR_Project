package com.example.EgarProject.services;

import com.example.EgarProject.models.Mail;
import com.example.EgarProject.pojo.MailDTO;
import com.example.EgarProject.repos.MailRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailService {
    private final JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String sender;
    private final MailRepo mailRepo;

    private final SpringTemplateEngine templateEngine;

    public MailService(JavaMailSender emailSender, MailRepo mailRepo, SpringTemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.mailRepo = mailRepo;
        this.templateEngine = templateEngine;
    }

    public ResponseEntity<String> sendMail(MailDTO mailDTO, BindingResult bindingResult) throws MessagingException {
        if (bindingResult.hasErrors()) {
            // Обработка ошибок валидации и возврат сообщений об ошибках на фронтенд
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }

        Mail mail = new Mail(sender,mailDTO.getReceiver(),mailDTO.getSubject(),mailDTO.getMessage() );
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mail.getSender());
            helper.setTo(mail.getReceiver());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getMessage());

            emailSender.send(mimeMessage);

            mailRepo.save(mail);
            return ResponseEntity.ok("Сообщение успешно доставлено");
        } catch (MessagingException | MailException e) {
            throw new MessagingException("Ошибка при отправке письма.", e);

        }
    }
}
