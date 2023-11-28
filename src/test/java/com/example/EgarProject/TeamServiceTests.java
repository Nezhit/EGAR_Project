package com.example.EgarProject;

import com.example.EgarProject.models.User;
import com.example.EgarProject.pojo.TaskLeadRequest;
import com.example.EgarProject.pojo.TeamDTO;
import com.example.EgarProject.repos.TeamRepo;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TeamService teamService;
    @Test
    @Order(0)
    public  void loadPostgresDumpIntoH2() {
        //посмотреть точно ли из H2 берутся данные + доп проверки сделать

        userRepo.findAll().stream().forEach(user -> System.out.println(user.getUsername()));
    }


    @Test
    @Order(3)
    public void testCreateTeam() {
        // создаем тестовые данные
        TeamDTO teamDTO = createTestTeamDTO();
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "teamDTO");

        // вызываем метод сервиса для создания команды
        ResponseEntity<String> responseEntity = teamService.createTeam(teamDTO, bindingResult);

        System.out.println(responseEntity.getBody());
        // проверяем, что команда создана успешно (HttpStatus.OK)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    @Order(2)
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
        TeamDTO teamDTO=new TeamDTO();
        teamDTO.setName("Team3");
        Set<User> users= new HashSet<>();
        users.add(userRepo.findByUsername("user").get());
        users.add(userRepo.findByUsername("user2").get());
        users.add(userRepo.findByUsername("user3").get());
        teamDTO.setTeamLead(userRepo.findByUsername("user5").get());
        teamDTO.setMembers(users);


        return teamDTO;
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
