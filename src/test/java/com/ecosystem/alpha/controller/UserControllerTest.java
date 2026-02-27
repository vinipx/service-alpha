package com.ecosystem.alpha.controller;

import com.ecosystem.alpha.service.UserService;
import com.ecosystem.common.dto.UserDto;
import com.ecosystem.common.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto buildUserDto(Long id) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setUsername("user" + id);
        dto.setEmail("user" + id + "@example.com");
        dto.setFullName("User " + id);
        dto.setCreatedAt(Instant.now());
        dto.setUpdatedAt(Instant.now());
        return dto;
    }

    @Test
    void shouldReturnAllUsers_whenGetAllUsers() throws Exception {
        // Arrange
        List<UserDto> users = List.of(buildUserDto(1L), buildUserDto(2L));
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void shouldReturnUser_whenGetUserById() throws Exception {
        // Arrange
        UserDto user = buildUserDto(1L);
        when(userService.getUserById(1L)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void shouldReturn404_whenGetUserByIdNotFound() throws Exception {
        // Arrange
        when(userService.getUserById(99L)).thenThrow(new NotFoundException("User not found with id: 99"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldCreateUser_whenPostUser() throws Exception {
        // Arrange
        UserDto input = new UserDto();
        input.setUsername("newuser");
        input.setEmail("newuser@example.com");
        input.setFullName("New User");

        UserDto created = buildUserDto(3L);
        when(userService.createUser(any(UserDto.class))).thenReturn(created);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(3));
    }

    @Test
    void shouldUpdateUser_whenPutUser() throws Exception {
        // Arrange
        UserDto input = new UserDto();
        input.setUsername("updated");
        input.setEmail("updated@example.com");
        input.setFullName("Updated User");

        UserDto updated = buildUserDto(1L);
        updated.setUsername("updated");
        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(updated);

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void shouldDeleteUser_whenDeleteUser() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUser(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404_whenDeleteUserNotFound() throws Exception {
        // Arrange
        doThrow(new NotFoundException("User not found with id: 99")).when(userService).deleteUser(99L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/99"))
                .andExpect(status().isNotFound());
    }
}
