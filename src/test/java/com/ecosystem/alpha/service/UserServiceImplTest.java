package com.ecosystem.alpha.service;

import com.ecosystem.alpha.model.User;
import com.ecosystem.alpha.repository.UserRepository;
import com.ecosystem.common.dto.UserDto;
import com.ecosystem.common.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User buildUser(Long id) {
        User user = new User("user" + id, "user" + id + "@example.com", "User " + id);
        user.setId(id);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return user;
    }

    @Test
    void shouldReturnAllUsers_whenGetAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(buildUser(1L), buildUser(2L)));

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertThat(result).hasSize(2);
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnUser_whenGetUserById() {
        // Arrange
        User user = buildUser(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        UserDto result = userService.getUserById(1L);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("user1");
    }

    @Test
    void shouldThrowNotFoundException_whenGetUserByIdNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldCreateUser_whenCreateUser() {
        // Arrange
        UserDto input = new UserDto(null, "newuser", "new@example.com", "New User", null, null);
        User saved = buildUser(1L);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        // Act
        UserDto result = userService.createUser(input);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldUpdateUser_whenUpdateUser() {
        // Arrange
        User existing = buildUser(1L);
        UserDto input = new UserDto(null, "updated", "updated@example.com", "Updated User", null, null);
        User updated = buildUser(1L);
        updated.setUsername("updated");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(updated);

        // Act
        UserDto result = userService.updateUser(1L, input);

        // Assert
        assertThat(result.getUsername()).isEqualTo("updated");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowNotFoundException_whenUpdateUserNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        UserDto input = new UserDto(null, "user", "user@example.com", "User", null, null);

        // Act & Assert
        assertThatThrownBy(() -> userService.updateUser(99L, input))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldDeleteUser_whenDeleteUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowNotFoundException_whenDeleteUserNotFound() {
        // Arrange
        when(userRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(NotFoundException.class);
    }
}
