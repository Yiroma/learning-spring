package org.wildcodeschool.myblog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.model.Image;

@Component
public class ImageMapper {

    public ImageDTO convertToDTO(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(image.getId());
        imageDTO.setUrl(image.getUrl());
        return imageDTO;
    }

}
