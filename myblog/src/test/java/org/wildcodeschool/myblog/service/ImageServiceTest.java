package org.wildcodeschool.myblog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.ImageMapper;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ImageRepository;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageMapper imageMapper;

    @InjectMocks
    private ImageService imageService;

    @Test
    void testGetAllImages() {
        // Arrange
        Image image1 = new Image();
        image1.setUrl("https://example.com/image1.jpg");

        Image image2 = new Image();
        image2.setUrl("https://example.com/image2.jpg");

        when(imageRepository.findAll()).thenReturn(List.of(image1, image2));

        ImageDTO dto1 = new ImageDTO();
        dto1.setUrl("https://example.com/image1.jpg");

        ImageDTO dto2 = new ImageDTO();
        dto2.setUrl("https://example.com/image2.jpg");

        when(imageMapper.convertToDTO(image1)).thenReturn(dto1);
        when(imageMapper.convertToDTO(image2)).thenReturn(dto2);

        // Act
        List<ImageDTO> images = imageService.getAllImages();

        // Assert
        assertThat(images).hasSize(2);
        assertThat(images.get(0).getUrl()).isEqualTo("https://example.com/image1.jpg");
        assertThat(images.get(1).getUrl()).isEqualTo("https://example.com/image2.jpg");
    }

    @Test
    void testGetImageById_ImageExists() {
        // Arrange
        Image image = new Image();
        image.setUrl("https://example.com/image1.jpg");

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        ImageDTO dto = new ImageDTO();
        dto.setUrl("https://example.com/image1.jpg");

        when(imageMapper.convertToDTO(image)).thenReturn(dto);

        // Act
        ImageDTO result = imageService.getImageById(1L);

        // Assert
        assertThat(result.getUrl()).isEqualTo("https://example.com/image1.jpg");
    }

    @Test
    void testGetImageById_ImageNotFound() {
        // Arrange
        when(imageRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> imageService.getImageById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Image with id 99 is not found");
    }

    @Test
    void testCreateImage() {
        // Arrange
        ImageDTO inputDTO = new ImageDTO();
        inputDTO.setUrl("https://example.com/new-image.jpg");

        Image savedImage = new Image();
        savedImage.setId(1L);
        savedImage.setUrl("https://example.com/new-image.jpg");

        when(imageRepository.save(any(Image.class))).thenReturn(savedImage);

        ImageDTO outputDTO = new ImageDTO();
        outputDTO.setId(1L);
        outputDTO.setUrl("https://example.com/new-image.jpg");

        when(imageMapper.convertToDTO(savedImage)).thenReturn(outputDTO);

        // Act
        ImageDTO result = imageService.createImage(inputDTO);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUrl()).isEqualTo("https://example.com/new-image.jpg");
    }

    @Test
    void testUpdateImage_ImageExists() {
        // Arrange
        Image existingImage = new Image();
        existingImage.setId(1L);
        existingImage.setUrl("https://example.com/old-image.jpg");

        when(imageRepository.findById(1L)).thenReturn(Optional.of(existingImage));

        ImageDTO updateDTO = new ImageDTO();
        updateDTO.setUrl("https://example.com/updated-image.jpg");

        Image updatedImage = new Image();
        updatedImage.setId(1L);
        updatedImage.setUrl("https://example.com/updated-image.jpg");

        when(imageRepository.save(existingImage)).thenReturn(updatedImage);

        ImageDTO outputDTO = new ImageDTO();
        outputDTO.setId(1L);
        outputDTO.setUrl("https://example.com/updated-image.jpg");

        when(imageMapper.convertToDTO(updatedImage)).thenReturn(outputDTO);

        // Act
        ImageDTO result = imageService.updateImage(1L, updateDTO);

        // Assert
        assertThat(result.getUrl()).isEqualTo("https://example.com/updated-image.jpg");
    }

    @Test
    void testUpdateImage_ImageNotFound() {
        // Arrange
        when(imageRepository.findById(99L)).thenReturn(Optional.empty());

        ImageDTO updateDTO = new ImageDTO();
        updateDTO.setUrl("https://example.com/updated-image.jpg");

        // Act & Assert
        assertThatThrownBy(() -> imageService.updateImage(99L, updateDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Image with id 99 is not found");
    }

    @Test
    void testDeleteImage_ImageExists() {
        // Arrange
        Image image = new Image();
        image.setId(1L);
        image.setUrl("https://example.com/image1.jpg");

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        // Act
        boolean result = imageService.deleteImage(1L);

        // Assert
        assertThat(result).isTrue();
        verify(imageRepository).delete(image);
    }

    @Test
    void testDeleteImage_ImageNotFound() {
        // Arrange
        when(imageRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> imageService.deleteImage(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Image with id 99 is not found");
    }
}
