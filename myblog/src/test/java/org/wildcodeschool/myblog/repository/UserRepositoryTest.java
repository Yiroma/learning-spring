package org.wildcodeschool.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.wildcodeschool.myblog.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");
        Optional<User> notFoundUser = userRepository.findByEmail("unknown@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        assertThat(notFoundUser).isEmpty();
    }

    @Test
    void testExistsByEmail() {
        // Arrange
        User user = new User();
        user.setEmail("existing@example.com");
        user.setPassword("password123");
        userRepository.save(user);

        // Act
        boolean exists = userRepository.existsByEmail("existing@example.com");
        boolean notExists = userRepository.existsByEmail("nonexistent@example.com");

        // Assert
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
