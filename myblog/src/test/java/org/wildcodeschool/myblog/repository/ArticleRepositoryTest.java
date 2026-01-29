package org.wildcodeschool.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.wildcodeschool.myblog.model.Article;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void testFindByTitle() {
        // Arrange
        Article article1 = new Article();
        article1.setTitle("Spring Boot Guide");
        article1.setContent("Content 1");
        article1.setCreatedAt(LocalDateTime.now());
        article1.setUpdatedAt(LocalDateTime.now());

        Article article2 = new Article();
        article2.setTitle("Spring Boot Guide");
        article2.setContent("Content 2");
        article2.setCreatedAt(LocalDateTime.now());
        article2.setUpdatedAt(LocalDateTime.now());

        Article article3 = new Article();
        article3.setTitle("Java Basics");
        article3.setContent("Content 3");
        article3.setCreatedAt(LocalDateTime.now());
        article3.setUpdatedAt(LocalDateTime.now());

        articleRepository.saveAll(List.of(article1, article2, article3));

        // Act
        List<Article> result = articleRepository.findByTitle("Spring Boot Guide");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(a -> a.getTitle().equals("Spring Boot Guide"));
    }

    @Test
    void testFindByContentContaining() {
        // Arrange
        Article article1 = new Article();
        article1.setTitle("Article 1");
        article1.setContent("Learn Spring Boot with examples");
        article1.setCreatedAt(LocalDateTime.now());
        article1.setUpdatedAt(LocalDateTime.now());

        Article article2 = new Article();
        article2.setTitle("Article 2");
        article2.setContent("Java programming basics");
        article2.setCreatedAt(LocalDateTime.now());
        article2.setUpdatedAt(LocalDateTime.now());

        Article article3 = new Article();
        article3.setTitle("Article 3");
        article3.setContent("Advanced Spring Boot techniques");
        article3.setCreatedAt(LocalDateTime.now());
        article3.setUpdatedAt(LocalDateTime.now());

        articleRepository.saveAll(List.of(article1, article2, article3));

        // Act
        List<Article> result = articleRepository.findByContentContaining("Spring Boot");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(a -> a.getContent().contains("Spring Boot"));
    }

    @Test
    void testFindByCreatedAtAfter() {
        // Arrange
        LocalDateTime referenceDate = LocalDateTime.of(2024, 6, 15, 0, 0);

        Article oldArticle = new Article();
        oldArticle.setTitle("Old Article");
        oldArticle.setContent("Old content");
        oldArticle.setCreatedAt(LocalDateTime.of(2024, 1, 1, 0, 0));
        oldArticle.setUpdatedAt(LocalDateTime.of(2024, 1, 1, 0, 0));

        Article newArticle1 = new Article();
        newArticle1.setTitle("New Article 1");
        newArticle1.setContent("New content 1");
        newArticle1.setCreatedAt(LocalDateTime.of(2024, 7, 1, 0, 0));
        newArticle1.setUpdatedAt(LocalDateTime.of(2024, 7, 1, 0, 0));

        Article newArticle2 = new Article();
        newArticle2.setTitle("New Article 2");
        newArticle2.setContent("New content 2");
        newArticle2.setCreatedAt(LocalDateTime.of(2024, 8, 1, 0, 0));
        newArticle2.setUpdatedAt(LocalDateTime.of(2024, 8, 1, 0, 0));

        articleRepository.saveAll(List.of(oldArticle, newArticle1, newArticle2));

        // Act
        List<Article> result = articleRepository.findByCreatedAtAfter(referenceDate);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(a -> a.getCreatedAt().isAfter(referenceDate));
    }

    @Test
    void testFindTop5ByOrderByCreatedAtDesc() {
        // Arrange
        for (int i = 1; i <= 7; i++) {
            Article article = new Article();
            article.setTitle("Article " + i);
            article.setContent("Content " + i);
            article.setCreatedAt(LocalDateTime.of(2024, i, 1, 0, 0));
            article.setUpdatedAt(LocalDateTime.of(2024, i, 1, 0, 0));
            articleRepository.save(article);
        }

        // Act
        List<Article> result = articleRepository.findTop5ByOrderByCreatedAtDesc();

        // Assert
        assertThat(result).hasSize(5);
        // Vérifie l'ordre décroissant (le plus récent en premier)
        assertThat(result.get(0).getTitle()).isEqualTo("Article 7");
        assertThat(result.get(1).getTitle()).isEqualTo("Article 6");
        assertThat(result.get(2).getTitle()).isEqualTo("Article 5");
        assertThat(result.get(3).getTitle()).isEqualTo("Article 4");
        assertThat(result.get(4).getTitle()).isEqualTo("Article 3");
    }
}
