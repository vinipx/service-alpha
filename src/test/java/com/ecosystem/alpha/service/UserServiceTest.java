package com.ecosystem.alpha.service;

import com.ecosystem.alpha.model.User;
import com.ecosystem.alpha.repository.UserRepository;
import com.ecosystem.common.dto.UserDto;
import com.ecosystem.common.exception.ConflictException;
import com.ecosystem.common.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private User sampleUser;
    private static final Instant NOW = Instant.parse("2024-01-01T00:00:00Z");

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("jdoe");
        sampleUser.setEmail("jdoe@example.com");
        sampleUser.setFullName("John Doe");
        sampleUser.setCreatedAt(NOW);
        sampleUser.setUpdatedAt(NOW);
    }

    @Test
    void shouldReturnAllUsers_whenFindAllCalled() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<UserDto> result = userService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).username()).isEqualTo("jdoe");
    }

    @Test
    void shouldReturnUser_whenFindByIdCalledWithExistingId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserDto result = userService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("jdoe@example.com");
    }

    @Test
    void shouldThrowNotFoundException_whenFindByIdCalledWithUnknownId() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldCreateUser_whenCreateCalledWithNonDuplicateData() {
        UserDto dto = new UserDto(null, "newuser", "new@example.com", "New User", null, null);
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserDto result = userService.create(dto);

        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowConflictException_whenCreateCalledWithDuplicateUsername() {
        UserDto dto = new UserDto(null, "jdoe", "other@example.com", "Other User", null, null);
        when(userRepository.existsByUsername("jdoe")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(dto))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("jdoe");
    }

    @Test
    void shouldThrowConflictException_whenCreateCalledWithDuplicateEmail() {
        UserDto dto = new UserDto(null, "other", "jdoe@example.com", "Other User", null, null);
        when(userRepository.existsByUsername("other")).thenReturn(false);
        when(userRepository.existsByEmail("jdoe@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(dto))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("jdoe@example.com");
    }

    @Test
    void shouldUpdateUser_whenUpdateCalledWithExistingId() {
        UserDto dto = new UserDto(null, "jdoe_updated", "jdoe_new@example.com", "John Updated", null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserDto result = userService.update(1L, dto);

        assertThat(result).isNotNull();
        verify(userRepository).save(sampleUser);
    }

    @Test
    void shouldThrowNotFoundException_whenUpdateCalledWithUnknownId() {
        UserDto dto = new UserDto(null, "x", "x@example.com", "X", null, null);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(99L, dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldDeleteUser_whenDeleteCalledWithExistingId() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteCalledWithUnknownId() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }
}
