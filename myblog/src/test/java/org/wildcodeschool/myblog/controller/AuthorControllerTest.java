package org.wildcodeschool.myblog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.security.JwtService;
import org.wildcodeschool.myblog.service.AuthorService;
import org.wildcodeschool.myblog.service.CustomUserDetailsService;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthorController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthorControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @MockitoBean
        private AuthorService authorService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        @Test
        void testGetAllAuthors() throws Exception {
                // Arrange
                AuthorDTO dto1 = new AuthorDTO();
                dto1.setFirstname("John");
                dto1.setLastname("Doe");

                AuthorDTO dto2 = new AuthorDTO();
                dto2.setFirstname("Jane");
                dto2.setLastname("Smith");

                when(authorService.getAllAuthors()).thenReturn(List.of(dto1, dto2));

                // Act & Assert
                mockMvc.perform(get("/authors"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].firstname").value("John"))
                                .andExpect(jsonPath("$[1].firstname").value("Jane"));
        }

        @Test
        void testGetAuthorById_AuthorExists() throws Exception {
                // Arrange
                AuthorDTO dto = new AuthorDTO();
                dto.setFirstname("John");
                dto.setLastname("Doe");

                when(authorService.getAuthorById(1L)).thenReturn(dto);

                // Act & Assert
                mockMvc.perform(get("/authors/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstname").value("John"))
                                .andExpect(jsonPath("$.lastname").value("Doe"));
        }

        @Test
        void testGetAuthorById_AuthorNotFound() throws Exception {
                // Arrange
                when(authorService.getAuthorById(99L))
                                .thenThrow(new ResourceNotFoundException("Author with id 99 is not found"));

                // Act & Assert
                mockMvc.perform(get("/authors/99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testCreateAuthor() throws Exception {
                // Arrange
                AuthorDTO inputDTO = new AuthorDTO();
                inputDTO.setFirstname("John");
                inputDTO.setLastname("Doe");

                AuthorDTO savedDTO = new AuthorDTO();
                savedDTO.setId(1L);
                savedDTO.setFirstname("John");
                savedDTO.setLastname("Doe");

                when(authorService.createAuthor(any(AuthorDTO.class))).thenReturn(savedDTO);

                // Act & Assert
                mockMvc.perform(post("/authors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstname").value("John"))
                                .andExpect(jsonPath("$.lastname").value("Doe"));
        }

        @Test
        void testUpdateAuthor_AuthorExists() throws Exception {
                // Arrange
                AuthorDTO updateDTO = new AuthorDTO();
                updateDTO.setFirstname("Johnny");
                updateDTO.setLastname("Updated");

                AuthorDTO updatedDTO = new AuthorDTO();
                updatedDTO.setId(1L);
                updatedDTO.setFirstname("Johnny");
                updatedDTO.setLastname("Updated");

                when(authorService.updateAuthor(eq(1L), any(AuthorDTO.class))).thenReturn(updatedDTO);

                // Act & Assert
                mockMvc.perform(put("/authors/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstname").value("Johnny"))
                                .andExpect(jsonPath("$.lastname").value("Updated"));
        }

        @Test
        void testUpdateAuthor_AuthorNotFound() throws Exception {
                // Arrange
                AuthorDTO updateDTO = new AuthorDTO();
                updateDTO.setFirstname("Johnny");
                updateDTO.setLastname("Updated");

                when(authorService.updateAuthor(eq(99L), any(AuthorDTO.class)))
                                .thenThrow(new ResourceNotFoundException("Author with id 99 is not found"));

                // Act & Assert
                mockMvc.perform(put("/authors/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDTO)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testDeleteAuthor_AuthorExists() throws Exception {
                // Arrange
                when(authorService.deleteAuthor(1L)).thenReturn(true);

                // Act & Assert
                mockMvc.perform(delete("/authors/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void testDeleteAuthor_AuthorNotFound() throws Exception {
                // Arrange
                when(authorService.deleteAuthor(99L))
                                .thenThrow(new ResourceNotFoundException("Author with id 99 is not found"));

                // Act & Assert
                mockMvc.perform(delete("/authors/99"))
                                .andExpect(status().isNotFound());
        }
}
