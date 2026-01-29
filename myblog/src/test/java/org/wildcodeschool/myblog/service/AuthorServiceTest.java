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
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.AuthorMapper;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.AuthorRepository;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void testGetAllAuthors() {
        // Arrange
        Author author1 = new Author();
        author1.setFirstname("John");
        author1.setLastname("Doe");

        Author author2 = new Author();
        author2.setFirstname("Jane");
        author2.setLastname("Smith");

        when(authorRepository.findAll()).thenReturn(List.of(author1, author2));

        AuthorDTO dto1 = new AuthorDTO();
        dto1.setFirstname("John");
        dto1.setLastname("Doe");

        AuthorDTO dto2 = new AuthorDTO();
        dto2.setFirstname("Jane");
        dto2.setLastname("Smith");

        when(authorMapper.convertToDTO(author1)).thenReturn(dto1);
        when(authorMapper.convertToDTO(author2)).thenReturn(dto2);

        // Act
        List<AuthorDTO> authors = authorService.getAllAuthors();

        // Assert
        assertThat(authors).hasSize(2);
        assertThat(authors.get(0).getFirstname()).isEqualTo("John");
        assertThat(authors.get(1).getFirstname()).isEqualTo("Jane");
    }

    @Test
    void testGetAuthorById_AuthorExists() {
        // Arrange
        Author author = new Author();
        author.setFirstname("John");
        author.setLastname("Doe");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorDTO dto = new AuthorDTO();
        dto.setFirstname("John");
        dto.setLastname("Doe");

        when(authorMapper.convertToDTO(author)).thenReturn(dto);

        // Act
        AuthorDTO result = authorService.getAuthorById(1L);

        // Assert
        assertThat(result.getFirstname()).isEqualTo("John");
        assertThat(result.getLastname()).isEqualTo("Doe");
    }

    @Test
    void testGetAuthorById_AuthorNotFound() {
        // Arrange
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authorService.getAuthorById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author with id 99 is not found");
    }

    @Test
    void testCreateAuthor() {
        // Arrange
        AuthorDTO inputDTO = new AuthorDTO();
        inputDTO.setFirstname("John");
        inputDTO.setLastname("Doe");

        Author savedAuthor = new Author();
        savedAuthor.setId(1L);
        savedAuthor.setFirstname("John");
        savedAuthor.setLastname("Doe");

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        AuthorDTO outputDTO = new AuthorDTO();
        outputDTO.setId(1L);
        outputDTO.setFirstname("John");
        outputDTO.setLastname("Doe");

        when(authorMapper.convertToDTO(savedAuthor)).thenReturn(outputDTO);

        // Act
        AuthorDTO result = authorService.createAuthor(inputDTO);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstname()).isEqualTo("John");
        assertThat(result.getLastname()).isEqualTo("Doe");
    }

    @Test
    void testUpdateAuthor_AuthorExists() {
        // Arrange
        Author existingAuthor = new Author();
        existingAuthor.setId(1L);
        existingAuthor.setFirstname("John");
        existingAuthor.setLastname("Doe");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));

        AuthorDTO updateDTO = new AuthorDTO();
        updateDTO.setFirstname("Johnny");
        updateDTO.setLastname("Updated");

        Author updatedAuthor = new Author();
        updatedAuthor.setId(1L);
        updatedAuthor.setFirstname("Johnny");
        updatedAuthor.setLastname("Updated");

        when(authorRepository.save(existingAuthor)).thenReturn(updatedAuthor);

        AuthorDTO outputDTO = new AuthorDTO();
        outputDTO.setId(1L);
        outputDTO.setFirstname("Johnny");
        outputDTO.setLastname("Updated");

        when(authorMapper.convertToDTO(updatedAuthor)).thenReturn(outputDTO);

        // Act
        AuthorDTO result = authorService.updateAuthor(1L, updateDTO);

        // Assert
        assertThat(result.getFirstname()).isEqualTo("Johnny");
        assertThat(result.getLastname()).isEqualTo("Updated");
    }

    @Test
    void testUpdateAuthor_AuthorNotFound() {
        // Arrange
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        AuthorDTO updateDTO = new AuthorDTO();
        updateDTO.setFirstname("Johnny");
        updateDTO.setLastname("Updated");

        // Act & Assert
        assertThatThrownBy(() -> authorService.updateAuthor(99L, updateDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author with id 99 is not found");
    }

    @Test
    void testDeleteAuthor_AuthorExists() {
        // Arrange
        Author author = new Author();
        author.setId(1L);
        author.setFirstname("John");
        author.setLastname("Doe");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        // Act
        boolean result = authorService.deleteAuthor(1L);

        // Assert
        assertThat(result).isTrue();
        verify(authorRepository).delete(author);
    }

    @Test
    void testDeleteAuthor_AuthorNotFound() {
        // Arrange
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authorService.deleteAuthor(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author with id 99 is not found");
    }
}
