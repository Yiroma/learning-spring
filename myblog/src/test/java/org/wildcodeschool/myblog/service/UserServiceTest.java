package org.wildcodeschool.myblog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wildcodeschool.myblog.model.User;
import org.wildcodeschool.myblog.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testFindById_UserExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindById_UserNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findById(99L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        String email = "newuser@example.com";
        String password = "password123";
        Set<String> roles = Set.of("ROLE_USER");

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(email);
        savedUser.setPassword("encodedPassword123");
        savedUser.setRoles(roles);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.registerUser(email, password, roles);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo("encodedPassword123");
        assertThat(result.getRoles()).contains("ROLE_USER");
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        String email = "existing@example.com";
        String password = "password123";
        Set<String> roles = Set.of("ROLE_USER");

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.registerUser(email, password, roles))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cet email est déjà utilisé");
    }
}
