package com.example.EgarProject;

import com.example.EgarProject.repos.ChangeJournalRepo;
import com.example.EgarProject.repos.TaskRepo;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.DocumentService;
import com.example.EgarProject.services.UserInfo;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)


@ActiveProfiles("test")
@SpringBootTest(properties = {"spring.config.name=hibernate-test", "spring.profiles.active=test"})
//@TestPropertySource("/hibernate-test.properties")
@Transactional
public class TestUserInfo {

    @Autowired
    private UserInfo userInfo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private ChangeJournalRepo changeJournalRepo;
@Autowired
private DocumentService documentService;
    @Transactional
    @Test
    @Sql(scripts = "classpath:clean.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = "classpath:testdata.sql", config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DirtiesContext
    public void testExtractUsername() {
        // Создаем HttpServletRequest с куками
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("username", "testUser"));

        // Вызываем метод и проверяем результат
        String username = userInfo.extractUsername(request);
        System.out.println(username);
        assertEquals("testUser", username);
    }

    @Test
    public void testGetInfo() {
        // Создайте HttpServletRequest с куками и передайте его в getInfo
        // Проверьте, что Optional<User> содержит правильного пользователя
    }

    @Test
    public void testGetUserTasksByUsername() {
        // Создайте HttpServletRequest с куками и передайте его в getUserTasksByUsername
        // Проверьте, что список ChangeJournal содержит правильные данные
    }

    @Test
    public void testGetMapUserTasksByUsername() {
        // Создайте HttpServletRequest с куками и передайте его в getMapUserTasksByUsername
        // Проверьте, что Map<Long, List<ChangeJournal>> содержит правильные данные
    }

    @Test
    public void testAssignTaskUser() {
        // Создайте HttpServletRequest с куками и передайте его в assignTaskUser
        // Проверьте, что задание успешно привязано к пользователю
    }

    @Test
    public void testCalculateCompletionPercentage() {
        // Создайте пользователя, создайте несколько задач с различными состояниями
        // Проверьте, что calculateCompletionPercentage возвращает правильное значение
    }

    @Test
    public void testCalculateAverageTaskCompletionTime() {
        // Создайте пользователя, создайте задачи с различными временными метками
        // Проверьте, что calculateAverageTaskCompletionTime возвращает правильное значение
    }

    @Test
    public void testCalculateTaskChangeFrequencyPerDay() {
        // Создайте пользователя, создайте несколько изменений с различными временными метками
        // Проверьте, что calculateTaskChangeFrequencyPerDay возвращает правильное значение
    }

    @Test
    public void testGetUsersWithoutTeam() {
        // Создайте пользователей с и без команды
        // Проверьте, что getUsersWithoutTeam возвращает правильный список пользователей
    }

    @Test
    public void testGetUsersFromSet() {
        // Создайте набор идентификаторов пользователей, передайте его в getUsersFromSet
        // Проверьте, что результат содержит правильные пользователей
    }
}
