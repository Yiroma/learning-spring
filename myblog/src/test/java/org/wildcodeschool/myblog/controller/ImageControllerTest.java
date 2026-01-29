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
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.security.JwtService;
import org.wildcodeschool.myblog.service.CustomUserDetailsService;
import org.wildcodeschool.myblog.service.ImageService;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ImageController.class)
@AutoConfigureMockMvc(addFilters = false)
class ImageControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @MockitoBean
        private ImageService imageService;

        @MockitoBean
        private JwtService jwtService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        @Test
        void testGetAllImages() throws Exception {
                // Arrange
                ImageDTO dto1 = new ImageDTO();
                dto1.setUrl("https://example.com/image1.jpg");

                ImageDTO dto2 = new ImageDTO();
                dto2.setUrl("https://example.com/image2.jpg");

                when(imageService.getAllImages()).thenReturn(List.of(dto1, dto2));

                // Act & Assert
                mockMvc.perform(get("/images"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].url").value("https://example.com/image1.jpg"))
                                .andExpect(jsonPath("$[1].url").value("https://example.com/image2.jpg"));
        }

        @Test
        void testGetAllImages_Empty() throws Exception {
                // Arrange
                when(imageService.getAllImages()).thenReturn(List.of());

                // Act & Assert
                mockMvc.perform(get("/images"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void testGetImageById_ImageExists() throws Exception {
                // Arrange
                ImageDTO dto = new ImageDTO();
                dto.setUrl("https://example.com/image1.jpg");

                when(imageService.getImageById(1L)).thenReturn(dto);

                // Act & Assert
                mockMvc.perform(get("/images/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.url").value("https://example.com/image1.jpg"));
        }

        @Test
        void testGetImageById_ImageNotFound() throws Exception {
                // Arrange
                when(imageService.getImageById(99L))
                                .thenThrow(new ResourceNotFoundException("Image with id 99 is not found"));

                // Act & Assert
                mockMvc.perform(get("/images/99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testCreateImage() throws Exception {
                // Arrange
                ImageDTO inputDTO = new ImageDTO();
                inputDTO.setUrl("https://example.com/new-image.jpg");

                ImageDTO savedDTO = new ImageDTO();
                savedDTO.setId(1L);
                savedDTO.setUrl("https://example.com/new-image.jpg");

                when(imageService.createImage(any(ImageDTO.class))).thenReturn(savedDTO);

                // Act & Assert
                mockMvc.perform(post("/images")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.url").value("https://example.com/new-image.jpg"));
        }

        @Test
        void testUpdateImage_ImageExists() throws Exception {
                // Arrange
                ImageDTO updateDTO = new ImageDTO();
                updateDTO.setUrl("https://example.com/updated-image.jpg");

                ImageDTO updatedDTO = new ImageDTO();
                updatedDTO.setId(1L);
                updatedDTO.setUrl("https://example.com/updated-image.jpg");

                when(imageService.updateImage(eq(1L), any(ImageDTO.class))).thenReturn(updatedDTO);

                // Act & Assert
                mockMvc.perform(put("/images/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.url").value("https://example.com/updated-image.jpg"));
        }

        @Test
        void testUpdateImage_ImageNotFound() throws Exception {
                // Arrange
                ImageDTO updateDTO = new ImageDTO();
                updateDTO.setUrl("https://example.com/updated-image.jpg");

                when(imageService.updateImage(eq(99L), any(ImageDTO.class)))
                                .thenThrow(new ResourceNotFoundException("Image with id 99 is not found"));

                // Act & Assert
                mockMvc.perform(put("/images/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDTO)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testDeleteImage_ImageExists() throws Exception {
                // Arrange
                when(imageService.deleteImage(1L)).thenReturn(true);

                // Act & Assert
                mockMvc.perform(delete("/images/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void testDeleteImage_ImageNotFound() throws Exception {
                // Arrange
                when(imageService.deleteImage(99L))
                                .thenThrow(new ResourceNotFoundException("Image with id 99 is not found"));

                // Act & Assert
                mockMvc.perform(delete("/images/99"))
                                .andExpect(status().isNotFound());
        }
}
