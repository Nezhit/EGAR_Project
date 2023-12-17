package com.example.EgarProject.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAllAccess() throws Exception {
        // Аутентификация пользователя и получение JWT-токена
        String jwtToken = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\", \"password\":\"1234\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Находим начальную позицию токена
        int startIndex = jwtToken.indexOf("\"token\":\"") + 9;

        // Находим конечную позицию токена
        int endIndex = jwtToken.indexOf("\",", startIndex);
        // Извлекаем значение токена
        jwtToken = jwtToken.substring(startIndex, endIndex);
        mockMvc.perform(get("/api/test/all").header("Authorization", "Bearer " + jwtToken))

                .andExpect(status().isOk())
                .andExpect(content().string("public API"));
    }

    @Test
    public void testUserAccess() throws Exception {
        // Аутентификация пользователя и получение JWT-токена
        String jwtToken = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\", \"password\":\"1234\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Находим начальную позицию токена
        int startIndex = jwtToken.indexOf("\"token\":\"") + 9;

        // Находим конечную позицию токена
        int endIndex = jwtToken.indexOf("\",", startIndex);
        // Извлекаем значение токена
        jwtToken = jwtToken.substring(startIndex, endIndex);
        mockMvc.perform(get("/api/test/user").header("Authorization", "Bearer " + jwtToken))

                .andExpect(status().isOk())
                .andExpect(content().string("user API"));
    }

    @Test
    public void testModeratorAccess() throws Exception {
        // Аутентификация пользователя и получение JWT-токена
        String jwtToken = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"HR1\", \"password\":\"1234\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Находим начальную позицию токена
        int startIndex = jwtToken.indexOf("\"token\":\"") + 9;

        // Находим конечную позицию токена
        int endIndex = jwtToken.indexOf("\",", startIndex);
        // Извлекаем значение токена
        jwtToken = jwtToken.substring(startIndex, endIndex);
        mockMvc.perform(get("/api/test/mod").header("Authorization", "Bearer " + jwtToken))

                .andExpect(status().isOk())
                .andExpect(content().string("moderator API"));
    }

    @Test
    public void testAdminAccess() throws Exception {
        // Аутентификация пользователя и получение JWT-токена
        String jwtToken = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user2\", \"password\":\"1234\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Находим начальную позицию токена
        int startIndex = jwtToken.indexOf("\"token\":\"") + 9;

        // Находим конечную позицию токена
        int endIndex = jwtToken.indexOf("\",", startIndex);
        // Извлекаем значение токена
        jwtToken = jwtToken.substring(startIndex, endIndex);
        mockMvc.perform(get("/api/test/admin").header("Authorization", "Bearer " + jwtToken))

                .andExpect(status().isOk())
                .andExpect(content().string("admin API"));
    }
}