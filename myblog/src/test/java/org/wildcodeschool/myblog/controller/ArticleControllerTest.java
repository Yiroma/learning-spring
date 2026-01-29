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
import org.wildcodeschool.myblog.dto.ArticleCreateDTO;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.dto.ArticleUpdateDTO;
import org.wildcodeschool.myblog.dto.AuthorContributionDTO;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.security.JwtService;
import org.wildcodeschool.myblog.service.ArticleService;
import org.wildcodeschool.myblog.service.CustomUserDetailsService;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArticleControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @MockitoBean
        private ArticleService articleService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        @Test
        void testGetAllArticles() throws Exception {
                // Arrange
                ArticleDTO dto1 = new ArticleDTO();
                dto1.setTitle("Article 1");
                dto1.setContent("Content 1");

                ArticleDTO dto2 = new ArticleDTO();
                dto2.setTitle("Article 2");
                dto2.setContent("Content 2");

                when(articleService.getAllArticles()).thenReturn(List.of(dto1, dto2));

                // Act & Assert
                mockMvc.perform(get("/articles"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].title").value("Article 1"))
                                .andExpect(jsonPath("$[1].title").value("Article 2"));
        }

        @Test
        void testGetAllArticles_Empty() throws Exception {
                // Arrange
                when(articleService.getAllArticles()).thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/articles"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void testGetArticleById_ArticleExists() throws Exception {
                // Arrange
                ArticleDTO dto = new ArticleDTO();
                dto.setTitle("Article 1");
                dto.setContent("Content 1");

                when(articleService.getArticleById(1L)).thenReturn(dto);

                // Act & Assert
                mockMvc.perform(get("/articles/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Article 1"))
                                .andExpect(jsonPath("$.content").value("Content 1"));
        }

        @Test
        void testGetArticleById_ArticleNotFound() throws Exception {
                // Arrange
                when(articleService.getArticleById(99L))
                                .thenThrow(new ResourceNotFoundException("Article with id 99 is not found"));

                // Act & Assert
                mockMvc.perform(get("/articles/99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testCreateArticle() throws Exception {
                // Arrange
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setUrl("https://example.com/image.jpg");

                AuthorContributionDTO authorDTO = new AuthorContributionDTO();
                authorDTO.setAuthorId(1L);
                authorDTO.setContribution("Main author");

                ArticleCreateDTO createDTO = new ArticleCreateDTO();
                createDTO.setTitle("New Article");
                createDTO.setContent("This is the content of the new article with enough characters");
                createDTO.setCategoryId(1L);
                createDTO.setImages(List.of(imageDTO));
                createDTO.setAuthors(List.of(authorDTO));

                ArticleDTO savedDTO = new ArticleDTO();
                savedDTO.setId(1L);
                savedDTO.setTitle("New Article");
                savedDTO.setContent("This is the content of the new article with enough characters");

                when(articleService.createArticle(any(ArticleCreateDTO.class))).thenReturn(savedDTO);

                // Act & Assert
                mockMvc.perform(post("/articles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.title").value("New Article"));
        }

        @Test
        void testUpdateArticle_ArticleExists() throws Exception {
                // Arrange
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setUrl("https://example.com/image.jpg");

                AuthorContributionDTO authorDTO = new AuthorContributionDTO();
                authorDTO.setAuthorId(1L);
                authorDTO.setContribution("Main author");

                ArticleUpdateDTO updateDTO = new ArticleUpdateDTO();
                updateDTO.setTitle("Updated Article");
                updateDTO.setContent("This is the updated content with enough characters");
                updateDTO.setCategoryId(1L);
                updateDTO.setImages(List.of(imageDTO));
                updateDTO.setAuthors(List.of(authorDTO));

                ArticleDTO updatedDTO = new ArticleDTO();
                updatedDTO.setId(1L);
                updatedDTO.setTitle("Updated Article");
                updatedDTO.setContent("This is the updated content with enough characters");

                when(articleService.updateArticle(eq(1L), any(ArticleUpdateDTO.class))).thenReturn(updatedDTO);

                // Act & Assert
                mockMvc.perform(put("/articles/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Updated Article"));
        }

        @Test
        void testDeleteArticle_ArticleExists() throws Exception {
                // Arrange
                when(articleService.deleteArticle(1L)).thenReturn(true);

                // Act & Assert
                mockMvc.perform(delete("/articles/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void testDeleteArticle_ArticleNotFound() throws Exception {
                // Arrange
                when(articleService.deleteArticle(99L))
                                .thenThrow(new ResourceNotFoundException("Article with id 99 is not found"));

                // Act & Assert
                mockMvc.perform(delete("/articles/99"))
                                .andExpect(status().isNotFound());
        }
}
