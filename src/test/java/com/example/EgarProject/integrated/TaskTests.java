package com.example.EgarProject.integrated;

import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.enums.EComplexity;
import com.example.EgarProject.pojo.TaskCreationRequest;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.repos.TeamRepo;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.HRService;
import com.example.EgarProject.services.TaskService;
import com.example.EgarProject.services.TeamService;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)


@ActiveProfiles("test")
@SpringBootTest(properties = {"spring.config.name=hibernate-test", "spring.profiles.active=test"})
@Transactional
public class TaskTests {
    @Autowired
    private TeamRepo teamRepo;
    @Autowired
    private TaskService taskService;

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
    @Sql(scripts = "classpath:clean.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = "classpath:testdata.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))

    @DirtiesContext
    public void usik(){//переписать на team логику
        TaskCreationRequest taskCreationRequest=new TaskCreationRequest();
        taskCreationRequest.setDeadline(LocalDateTime.now());
        taskCreationRequest.setDescription("Dast");
        taskCreationRequest.setComplexity(EComplexity.EPIC);
        hrService.createTask(taskCreationRequest);

    }
    @Test
    @Transactional

    @Sql(scripts = "classpath:clean.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = "classpath:testdata.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))

    @DirtiesContext
    public void testFindTaskWithoutUser() {
        // Добавим тестовые данные в репозиторий задач
        Task task1 = taskRepo.findById(5L).get();
        Task task2 = taskRepo.findById(12L).get();
        taskRepo.saveAll(List.of(task1, task2));

        // Вызываем метод сервиса
        List<Task> tasksWithoutUser = taskService.findTaskWithoutUser();

        // Проверяем, что список задач без пользователя не пустой
        assertNotNull(tasksWithoutUser);
        // Проверяем, что все задачи в списке не имеют пользователя
        tasksWithoutUser.forEach(task -> assertEquals(null, task.getUser()));
    }

}
