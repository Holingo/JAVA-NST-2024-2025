package org.example.projectmanagerapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.enums.UserRole;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private ObjectMapper objectMapper;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("jan");
        sampleUser.setPassword("pass");
        sampleUser.setRole(UserRole.USER);
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(sampleUser));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("jan"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(sampleUser);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jan"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jan"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jan"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());
    }
}