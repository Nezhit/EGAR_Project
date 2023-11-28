package com.example.EgarProject.models;

import com.example.EgarProject.models.enums.ESpecialization;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users", uniqueConstraints = {@UniqueConstraint(columnNames="username"),
@UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "papaname")
    private String papaname;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles=new HashSet<>();
    @JsonIgnore
    //--------------------------
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<ChangeJournal> changes = new HashSet<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
    private ESpecialization specialization;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public User() {
    }

    public User(String username, String email, String password,ESpecialization specialization) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.specialization=specialization;
    }

    public User(String username, String email, String password, String name, String surname, String papaname, ESpecialization specialization) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.papaname = papaname;
        this.specialization = specialization;
    }

    public ESpecialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(ESpecialization specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
