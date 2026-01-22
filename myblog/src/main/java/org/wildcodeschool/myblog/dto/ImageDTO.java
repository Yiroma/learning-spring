package org.wildcodeschool.myblog.dto;

import org.hibernate.validator.constraints.URL;

public class ImageDTO {

    private Long id;

    @URL(message = "URL must be valid")
    private String url;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
