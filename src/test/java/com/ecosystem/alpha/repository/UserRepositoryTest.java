package com.ecosystem.alpha.repository;

import com.ecosystem.alpha.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User saveUser(String username, String email, String fullName) {
        User user = new User(username, email, fullName);
        return userRepository.save(user);
    }

    @Test
    void shouldFindByUsername_whenUserExists() {
        // Arrange
        saveUser("testuser", "test@example.com", "Test User");

        // Act
        Optional<User> result = userRepository.findByUsername("testuser");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldReturnEmpty_whenFindByUsernameNotExists() {
        // Act
        Optional<User> result = userRepository.findByUsername("nonexistent");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindByEmail_whenUserExists() {
        // Arrange
        saveUser("emailuser", "findme@example.com", "Email User");

        // Act
        Optional<User> result = userRepository.findByEmail("findme@example.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("findme@example.com");
    }

    @Test
    void shouldReturnTrue_whenExistsByUsername() {
        // Arrange
        saveUser("existsuser", "exists@example.com", "Exists User");

        // Act
        boolean exists = userRepository.existsByUsername("existsuser");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalse_whenNotExistsByUsername() {
        // Act
        boolean exists = userRepository.existsByUsername("ghost");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void shouldReturnTrue_whenExistsByEmail() {
        // Arrange
        saveUser("emailcheck", "check@example.com", "Check User");

        // Act
        boolean exists = userRepository.existsByEmail("check@example.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void shouldPersistCreatedAtAndUpdatedAt_whenUserSaved() {
        // Arrange & Act
        User saved = saveUser("tsuser", "ts@example.com", "TS User");

        // Assert
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }
}
