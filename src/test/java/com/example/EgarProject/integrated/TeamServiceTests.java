package com.example.EgarProject.integrated;

import com.example.EgarProject.models.User;
import com.example.EgarProject.pojo.TaskCreationRequest;
import com.example.EgarProject.pojo.TaskLeadRequest;
import com.example.EgarProject.pojo.TeamDTO;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.repos.TeamRepo;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.HRService;
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
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@RunWith(SpringJUnit4ClassRunner.class)


@ActiveProfiles("test")
@SpringBootTest(properties = {"spring.config.name=hibernate-test", "spring.profiles.active=test"})
//@TestPropertySource("/hibernate-test.properties")
@Transactional

public class TeamServiceTests {
    @Autowired
    private TeamRepo teamRepo;

    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    HRService hrService;

    @Autowired
    private TeamService teamService;
    @Transactional
    @Test
    @Order(1)
    @Sql(scripts = "classpath:clean.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = "classpath:testdata.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DirtiesContext
    public  void loadPostgresDumpIntoH2() {
        //посмотреть точно ли из H2 берутся данные + доп проверки сделать

        userRepo.findAll().stream().forEach(user -> System.out.println(user.getId()+" "+user.getUsername()));
    }

    @Transactional
    @Test
    @Order(2)
    @Sql(scripts = "classpath:clean.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = "classpath:testdata.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DirtiesContext
    public void testCreateTeam() {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "taskLeadRequest");

        TeamDTO teamDTO= createTestTeamDTO();


//
//        // вызываем метод сервиса для создания команды

         teamService.createTeam(teamDTO, bindingResult);
         teamDTO.setTeamLead(userRepo.findById(7L).get().getId());
        teamDTO.setName("titit");
        teamService.createTeam(teamDTO, bindingResult);
        teamRepo.findAll().forEach(team -> System.out.println(team.getId()+" = "+ team.getName()));

    }
    @Transactional
    @Test
    @Order(4)
    @Sql(scripts = "classpath:clean.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = "classpath:testdata.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))

    @DirtiesContext
    public void usik(){//переписать на team логику
        TaskCreationRequest taskCreationRequest=new TaskCreationRequest();
        taskCreationRequest.setDeadline(LocalDateTime.now());
        taskCreationRequest.setDescription("Dast");
        hrService.createTask(taskCreationRequest);

    }
    @Transactional
    @Test
    @Order(3)
    @Sql(scripts = "classpath:clean.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = "classpath:testdata.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DirtiesContext
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
    @Transactional
    @Test
    @Order(3)
    @Sql(scripts = "classpath:clean.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = "classpath:testdata.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DirtiesContext
    public void testGetTeamMembersByLeader(){
        testCreateTeam();
        // Вызов метода сервиса для получения участников команды
        List<User> teamMembers = teamService.getTeamMembersByLeader(6L);

        // Проверка, что список участников не пустой и содержит ожидаемые элементы
        assertFalse(teamMembers.isEmpty());
    }
    private TeamDTO createTestTeamDTO() {
        String json="{\n" +
                "  \"name\": \"Команда 1\",\n" +
                "  \"members\": [1, 2, 3],\n" +
                "  \"teamLead\": 6\n" +
                "}";
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
