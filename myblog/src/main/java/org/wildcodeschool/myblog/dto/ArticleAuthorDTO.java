package org.wildcodeschool.myblog.dto;

public class ArticleAuthorDTO {
    private Long id;
    private String contribution;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContribution() {
        return contribution;
    }

    public void setContribution(String contribution) {
        this.contribution = contribution;
    }
}
