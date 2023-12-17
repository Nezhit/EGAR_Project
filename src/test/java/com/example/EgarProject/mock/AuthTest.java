package com.example.EgarProject.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAuthenticationAndAccessToProtectedEndpoints() throws Exception {
        // Аутентификация пользователя и получение JWT-токена
        String jwtToken = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\", \"password\":\"1234\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(jwtToken);

        // Находим начальную позицию токена
        int startIndex = jwtToken.indexOf("\"token\":\"") + 9;

        // Находим конечную позицию токена
        int endIndex = jwtToken.indexOf("\",", startIndex);

        // Извлекаем значение токена
         jwtToken = jwtToken.substring(startIndex, endIndex);

        // Выводим значение токена
        System.out.println(" " + jwtToken);

        // Попытка доступа к защищенному ресурсу с использованием полученного токена
        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/user")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

        /*
        // Попытка доступа к другим защищенным ресурсам (moderator и admin) с использованием полученного токена
        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/mod")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/test/admin")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

         */
    }
}
