package org.wildcodeschool.myblog.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public class ArticleCreateDTO {

    @NotBlank(message = "Title must not be blank")
    @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters")
    private String title;

    @NotBlank(message = "Content must not be blank")
    @Size(min = 10, message = "Content must be at least 10 characters long")
    private String content;

    @NotNull(message = "Category ID must not be null")
    @Positive(message = "Category ID must be a positive number")
    private Long categoryId;

    @NotEmpty(message = "Images list must not be empty")
    private List<@Valid ImageDTO> images;

    @NotEmpty(message = "Authors list must not be empty")
    private List<@Valid AuthorContributionDTO> authors;

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

    public List<AuthorContributionDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorContributionDTO> authors) {
        this.authors = authors;
    }

}
