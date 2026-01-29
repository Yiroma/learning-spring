package org.wildcodeschool.myblog.controller;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.wildcodeschool.myblog.dto.UserLoginDTO;
import org.wildcodeschool.myblog.dto.UserRegistrationDTO;
import org.wildcodeschool.myblog.model.User;
import org.wildcodeschool.myblog.security.AuthenticationService;
import org.wildcodeschool.myblog.security.JwtService;
import org.wildcodeschool.myblog.service.CustomUserDetailsService;
import org.wildcodeschool.myblog.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @MockitoBean
        private UserService userService;

        @MockitoBean
        private AuthenticationService authenticationService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        @Test
        void testRegister_Success() throws Exception {
                // Arrange
                UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
                registrationDTO.setEmail("newuser@example.com");
                registrationDTO.setPassword("password123");

                User registeredUser = new User();
                registeredUser.setId(1L);
                registeredUser.setEmail("newuser@example.com");
                registeredUser.setRoles(Set.of("ROLE_USER"));

                when(userService.registerUser(anyString(), anyString(), anySet())).thenReturn(registeredUser);

                // Act & Assert
                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.email").value("newuser@example.com"));
        }

        @Test
        void testRegister_EmailAlreadyExists() throws Exception {
                // Arrange
                UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
                registrationDTO.setEmail("existing@example.com");
                registrationDTO.setPassword("password123");

                when(userService.registerUser(anyString(), anyString(), anySet()))
                                .thenThrow(new RuntimeException("Cet email est déjà utilisé"));

                // Act & Assert
                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationDTO)))
                                .andExpect(status().isInternalServerError());
        }

        @Test
        void testLogin_Success() throws Exception {
                // Arrange
                UserLoginDTO loginDTO = new UserLoginDTO();
                loginDTO.setEmail("user@example.com");
                loginDTO.setPassword("password123");

                String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";

                when(authenticationService.authenticate(anyString(), anyString())).thenReturn(jwtToken);

                // Act & Assert
                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO)))
                                .andExpect(status().isOk())
                                .andExpect(content().string(jwtToken));
        }

        @Test
        void testLogin_InvalidCredentials() throws Exception {
                // Arrange
                UserLoginDTO loginDTO = new UserLoginDTO();
                loginDTO.setEmail("user@example.com");
                loginDTO.setPassword("wrongpassword");

                when(authenticationService.authenticate(anyString(), anyString()))
                                .thenThrow(new RuntimeException("Invalid credentials"));

                // Act & Assert
                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO)))
                                .andExpect(status().isInternalServerError());
        }
}
