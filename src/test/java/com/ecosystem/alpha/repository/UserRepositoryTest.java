package com.ecosystem.alpha.repository;

import com.ecosystem.alpha.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("jdoe");
        user.setEmail("jdoe@example.com");
        user.setFullName("John Doe");
        savedUser = userRepository.save(user);
    }

    @Test
    void shouldSaveUser_whenValidUserProvided() {
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFindUserByUsername_whenUsernameExists() {
        Optional<User> result = userRepository.findByUsername("jdoe");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("jdoe@example.com");
    }

    @Test
    void shouldReturnEmpty_whenFindByUsernameCalledWithUnknownUsername() {
        Optional<User> result = userRepository.findByUsername("unknown");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindUserByEmail_whenEmailExists() {
        Optional<User> result = userRepository.findByEmail("jdoe@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("jdoe");
    }

    @Test
    void shouldReturnEmpty_whenFindByEmailCalledWithUnknownEmail() {
        Optional<User> result = userRepository.findByEmail("nobody@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnTrue_whenExistsByUsernameCalledWithExistingUsername() {
        assertThat(userRepository.existsByUsername("jdoe")).isTrue();
    }

    @Test
    void shouldReturnFalse_whenExistsByUsernameCalledWithNonExistingUsername() {
        assertThat(userRepository.existsByUsername("ghost")).isFalse();
    }

    @Test
    void shouldReturnTrue_whenExistsByEmailCalledWithExistingEmail() {
        assertThat(userRepository.existsByEmail("jdoe@example.com")).isTrue();
    }

    @Test
    void shouldReturnFalse_whenExistsByEmailCalledWithNonExistingEmail() {
        assertThat(userRepository.existsByEmail("ghost@example.com")).isFalse();
    }
}
