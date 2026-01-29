
package org.wildcodeschool.myblog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.wildcodeschool.myblog.model.Category;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testFindAllCategories() {
        // Arrange : create and save some categories in the test database
        Category category1 = new Category();
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setName("Category 2");

        categoryRepository.saveAll(List.of(category1, category2));

        // Act : retrieve all categories from the repository
        List<Category> categories = categoryRepository.findAll();

        // Assert : verify that the returned list contains the categories
        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).getName()).isEqualTo("Category 1");
        assertThat(categories.get(1).getName()).isEqualTo("Category 2");
    }
}