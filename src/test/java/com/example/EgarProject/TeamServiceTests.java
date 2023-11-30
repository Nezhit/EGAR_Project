package com.example.EgarProject;

import com.example.EgarProject.models.User;
import com.example.EgarProject.pojo.TaskLeadRequest;
import com.example.EgarProject.pojo.TeamDTO;
import com.example.EgarProject.repos.TeamRepo;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)

@TestPropertySource("/hibernate-test.properties")
@ActiveProfiles("test")
@SpringBootTest(properties = {"spring.config.name=hibernate-test", "spring.profiles.active=test"})
@Transactional
@Sql("classpath:testdata.sql")
public class TeamServiceTests {
    @Autowired
    private TeamRepo teamRepo;


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TeamService teamService;
    @Transactional
    @Test
    @Order(1)
    public  void loadPostgresDumpIntoH2() {
        //посмотреть точно ли из H2 берутся данные + доп проверки сделать

        userRepo.findAll().stream().forEach(user -> System.out.println(user.getUsername()));
    }

    @Transactional
    @Test
    @Order(2)
    public void testCreateTeam() {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "taskLeadRequest");

        TeamDTO teamDTO= createTestTeamDTO();


//
//        // вызываем метод сервиса для создания команды

         teamService.createTeam(teamDTO, bindingResult);
         teamDTO.setTeamLead(userRepo.findByUsername("user7").get());
        teamDTO.setName("titit");
        teamService.createTeam(teamDTO, bindingResult);
        teamRepo.findAll().forEach(team -> System.out.println(team.getId()+" = "+ team.getName()));

    }
    @Transactional
    @Test
    @Order(4)
    public void us(){
        User user= new User();
        user.setPassword("$2a$10$3uVm5kNjNakctk1dbfI3ROOlKqunBAZgrISaSG/Bsjkh4dv5jZQNa");
        user.setName("user9");
        user.setEmail("user9@mail.ru");
        userRepo.saveAndFlush(user);
        userRepo.findAll().forEach(user1 -> System.out.println(user1.getUsername()));
    }
    @Transactional
    @Test
    @Order(3)
    public void testAppointLead() {
        // создаем тестовые данные
        TaskLeadRequest taskLeadRequest = createTestTaskLeadRequest(1);
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "taskLeadRequest");
        assert taskLeadRequest != null;


        // вызываем метод сервиса для назначения лидера
        ResponseEntity<String> responseEntity = teamService.appointLead(taskLeadRequest, bindingResult);

        System.out.println("Ответ= "+responseEntity.getBody());
        // проверяем, что лидер назначен успешно (HttpStatus.OK)
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        taskLeadRequest = createTestTaskLeadRequest(2);
        responseEntity = teamService.appointLead(taskLeadRequest, bindingResult);
        System.out.println("Ответ= "+responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        taskLeadRequest = createTestTaskLeadRequest(3);
        responseEntity = teamService.appointLead(taskLeadRequest, bindingResult);
        System.out.println("Ответ= "+responseEntity.getBody());
        // проверяем, что лидер назначен успешно (HttpStatus.OK)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        // добавьте здесь дополнительные проверки, если это необходимо
    }

    private TeamDTO createTestTeamDTO() {
        String json="{\n" +
                "  \"name\": \"Team3\",\n" +
                "  \"members\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"username\": \"user\",\n" +
                "      \"email\": \"user@mail.ru\",\n" +
                "      \"password\": \"1234\",\n" +
                "      \"specialization\": \"JUNIOR\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"username\": \"user2\",\n" +
                "      \"email\": \"user2@mail.ru\",\n" +
                "      \"password\": \"1234\",\n" +
                "      \"specialization\": \"JUNIOR\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 3,\n" +
                "      \"username\": \"user3\",\n" +
                "      \"email\": \"user3@mail.ru\",\n" +
                "      \"password\": \"1234\",\n" +
                "      \"specialization\": \"SENIOR\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"teamLead\": {\n" +
                "    \"id\": 6,\n" +
                "    \"username\": \"user5\",\n" +
                "    \"email\": \"user5@mail.ru\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"specialization\": \"TEAM_LEAD\"\n" +
                "  }\n" +
                "}\n";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, TeamDTO.class);
        } catch (Exception e) {
            // обработка ошибок при парсинге JSON
            e.printStackTrace();
            return null;
        }



    }

    private TaskLeadRequest createTestTaskLeadRequest(int x) {
        String json;
        switch (x){
            case 1:
                json="{\n" +
                        "    \n" +
                        "    \"username\":\"HR1\",\n" +
                        "    \"teamName\":\"Team1\"\n" +
                        "}";
                break;
            case 2:
                json="{\n" +
                        "    \n" +
                        "    \"username\":\"user5\",\n" +
                        "    \"teamName\":\"Team1\"\n" +
                        "}";
                break;
            case 3:
                json="{\n" +
                        "    \n" +
                        "    \"username\":\"user7\",\n" +
                        "    \"teamName\":\"Team1\"\n" +
                        "}";
                break;
            case 4:
                json="{\n" +
                        "    \n" +
                        "    \"username\":\"user7\",\n" +
                        "    \"teamName\":\"Team2\"\n" +
                        "}";
                break;
            default:
                json="{\n" +
                        "    \n" +
                        "    \"username\":\"user4\",\n" +
                        "    \"teamName\":\"Team2\"\n" +
                        "}";
                break;
        }


        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, TaskLeadRequest.class);
        } catch (Exception e) {
            // обработка ошибок при парсинге JSON
            e.printStackTrace();
            return null;
        }
    }
}
