package org.wildcodeschool.myblog.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.dto.ArticleCreateDTO;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.model.Image;

@Component
public class ArticleMapper {

    public ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        articleDTO.setUpdatedAt(article.getUpdatedAt());
        if (article.getCategory() != null) {
            articleDTO.setCategoryName(article.getCategory().getName());
        }
        if (article.getImages() != null) {
            articleDTO.setImageUrls(article.getImages().stream().map(Image::getUrl).collect(Collectors.toList()));
        }
        if (article.getArticleAuthors() != null) {
            articleDTO.setAuthors(article.getArticleAuthors().stream()
                    .filter(articleAuthor -> articleAuthor.getAuthor() != null)
                    .map(articleAuthor -> {
                        AuthorDTO authorDTO = new AuthorDTO();
                        authorDTO.setId(articleAuthor.getAuthor().getId());
                        authorDTO.setFirstname(articleAuthor.getAuthor().getFirstname());
                        authorDTO.setLastname(articleAuthor.getAuthor().getLastname());
                        return authorDTO;
                    })
                    .collect(Collectors.toList()));
        }
        return articleDTO;
    }

    public Article convertToEntity(ArticleCreateDTO dto) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            article.setCategory(category);
        }

        if (dto.getImages() != null) {
            List<Image> images = dto.getImages().stream().map(imageDTO -> {
                Image image = new Image();
                image.setId(imageDTO.getId());
                image.setUrl(imageDTO.getUrl());
                return image;
            }).collect(Collectors.toList());
            article.setImages(images);
        }

        if (dto.getAuthors() != null) {
            List<ArticleAuthor> articleAuthors = dto.getAuthors().stream().map(authorDTO -> {
                ArticleAuthor articleAuthor = new ArticleAuthor();
                Author author = new Author();
                author.setId(authorDTO.getAuthorId());
                articleAuthor.setAuthor(author);
                articleAuthor.setContribution(authorDTO.getContribution());
                return articleAuthor;
            }).collect(Collectors.toList());
            article.setArticleAuthors(articleAuthors);
        }

        return article;
    }

}
