package org.wildcodeschool.myblog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.ImageMapper;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ImageRepository;

@Service
public class ImageService {

    private final ImageMapper imageMapper;
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public List<ImageDTO> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(imageMapper::convertToDTO).collect(Collectors.toList());
    }

    public ImageDTO getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image with id " + id + " is not found"));
        return imageMapper.convertToDTO(image);
    }

    public ImageDTO createImage(ImageDTO imageDTO) {
        Image image = new Image();
        image.setUrl(imageDTO.getUrl());
        Image savedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(savedImage);
    }

    public ImageDTO updateImage(Long id, ImageDTO imageDTO) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image with id " + id + " is not found"));
        image.setUrl(imageDTO.getUrl());
        Image updatedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(updatedImage);
    }

    public boolean deleteImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image with id " + id + " is not found"));
        imageRepository.delete(image);
        return true;
    }

}
