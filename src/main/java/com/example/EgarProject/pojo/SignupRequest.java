package com.example.EgarProject.pojo;

import com.example.EgarProject.models.enums.ESpecialization;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public class SignupRequest {
    @NotBlank(message = "Имя пользователя не должно быть пустым")
    @Pattern(regexp = "\\S+", message = "Имя пользователя не должно состоять только из пробелов")
    private String username;
    private String email;
    private Set<String> roles;
    private String password;
    private String name;
    private String surname;
    private String papaname;
    private ESpecialization specialization;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPapaname() {
        return papaname;
    }

    public void setPapaname(String papaname) {
        this.papaname = papaname;
    }

    public ESpecialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(ESpecialization specialization) {
        this.specialization = specialization;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", papaname='" + papaname + '\'' +
                ", specialization=" + specialization +
                '}';
    }
}
