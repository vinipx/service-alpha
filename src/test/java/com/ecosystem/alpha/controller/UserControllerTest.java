package com.ecosystem.alpha.controller;

import com.ecosystem.alpha.service.UserService;
import com.ecosystem.common.dto.UserDto;
import com.ecosystem.common.exception.ConflictException;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final UserDto SAMPLE_USER = new UserDto(
            1L, "jdoe", "jdoe@example.com", "John Doe",
            Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));

    @Test
    void shouldReturnAllUsers_whenGetAllEndpointCalled() throws Exception {
        when(userService.findAll()).thenReturn(List.of(SAMPLE_USER));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].username").value("jdoe"));
    }

    @Test
    void shouldReturnUser_whenGetByIdCalledWithValidId() throws Exception {
        when(userService.findById(1L)).thenReturn(SAMPLE_USER);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("jdoe"));
    }

    @Test
    void shouldReturn404_whenGetByIdCalledWithUnknownId() throws Exception {
        when(userService.findById(99L)).thenThrow(new NotFoundException("User not found with id: 99"));

        mockMvc.perform(get("/api/v1/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldCreateUser_whenPostCalledWithValidBody() throws Exception {
        when(userService.create(any(UserDto.class))).thenReturn(SAMPLE_USER);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SAMPLE_USER)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("jdoe@example.com"));
    }

    @Test
    void shouldReturn409_whenCreateCalledWithDuplicateUsername() throws Exception {
        when(userService.create(any(UserDto.class)))
                .thenThrow(new ConflictException("Username already exists: jdoe"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SAMPLE_USER)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldUpdateUser_whenPutCalledWithValidIdAndBody() throws Exception {
        when(userService.update(eq(1L), any(UserDto.class))).thenReturn(SAMPLE_USER);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SAMPLE_USER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.fullName").value("John Doe"));
    }

    @Test
    void shouldReturn404_whenUpdateCalledWithUnknownId() throws Exception {
        when(userService.update(eq(99L), any(UserDto.class)))
                .thenThrow(new NotFoundException("User not found with id: 99"));

        mockMvc.perform(put("/api/v1/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SAMPLE_USER)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldDeleteUser_whenDeleteCalledWithValidId() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404_whenDeleteCalledWithUnknownId() throws Exception {
        doThrow(new NotFoundException("User not found with id: 99")).when(userService).delete(99L);

        mockMvc.perform(delete("/api/v1/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
