package org.wildcodeschool.myblog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.ArticleMapper;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;
import org.wildcodeschool.myblog.repository.CategoryRepository;
import org.wildcodeschool.myblog.repository.ImageRepository;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleMapper articleMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private ArticleAuthorRepository articleAuthorRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    void testGetAllArticles() {
        // Arrange
        Article article1 = new Article();
        article1.setTitle("Article 1");
        article1.setContent("Content 1");
        article1.setCreatedAt(LocalDateTime.now());
        article1.setUpdatedAt(LocalDateTime.now());

        Article article2 = new Article();
        article2.setTitle("Article 2");
        article2.setContent("Content 2");
        article2.setCreatedAt(LocalDateTime.now());
        article2.setUpdatedAt(LocalDateTime.now());

        when(articleRepository.findAll()).thenReturn(List.of(article1, article2));

        ArticleDTO dto1 = new ArticleDTO();
        dto1.setTitle("Article 1");
        dto1.setContent("Content 1");

        ArticleDTO dto2 = new ArticleDTO();
        dto2.setTitle("Article 2");
        dto2.setContent("Content 2");

        when(articleMapper.convertToDTO(article1)).thenReturn(dto1);
        when(articleMapper.convertToDTO(article2)).thenReturn(dto2);

        // Act
        List<ArticleDTO> articles = articleService.getAllArticles();

        // Assert
        assertThat(articles).hasSize(2);
        assertThat(articles.get(0).getTitle()).isEqualTo("Article 1");
        assertThat(articles.get(1).getTitle()).isEqualTo("Article 2");
    }

    @Test
    void testGetArticleById_ArticleExists() {
        // Arrange
        Article article = new Article();
        article.setTitle("Article 1");
        article.setContent("Content 1");
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        ArticleDTO dto = new ArticleDTO();
        dto.setTitle("Article 1");
        dto.setContent("Content 1");

        when(articleMapper.convertToDTO(article)).thenReturn(dto);

        // Act
        ArticleDTO result = articleService.getArticleById(1L);

        // Assert
        assertThat(result.getTitle()).isEqualTo("Article 1");
        assertThat(result.getContent()).isEqualTo("Content 1");
    }

    @Test
    void testGetArticleById_ArticleNotFound() {
        // Arrange
        when(articleRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> articleService.getArticleById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Article with id 99 is not found");
    }

    @Test
    void testDeleteArticle_ArticleExists() {
        // Arrange
        Article article = new Article();
        article.setTitle("Article 1");
        article.setArticleAuthors(List.of());

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        // Act
        boolean result = articleService.deleteArticle(1L);

        // Assert
        assertThat(result).isTrue();
        verify(articleRepository).delete(article);
    }

    @Test
    void testDeleteArticle_ArticleNotFound() {
        // Arrange
        when(articleRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> articleService.deleteArticle(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Article with id 99 is not found");
    }
}
