package com.example.testapp.service;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.exceptions.EntityNotFoundException;
import com.example.testapp.model.Authors;
import com.example.testapp.model.Books;
import com.example.testapp.repository.AuthorsRepository;
import com.example.testapp.repository.BooksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorsRepository authorsRepository;

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void addAuthor_Success() {

        Authors authors = new Authors();
        authors.setId(1L);
        authors.setName("John Doe");
        authors.setBookList(List.of(1L, 2L, 3L));

        when(authorsRepository.save(any(Authors.class))).thenReturn(authors);

        AuthorDTO authorDTO = authorService.addAuthor(AuthorDTO.fromEntity(authors));

        assertNotNull(authorDTO);
        assertEquals("John Doe", authorDTO.getName());
        verify(authorsRepository, times(1)).save(any(Authors.class));
    }

    @Test
    void getAuthorById_Success() {
        long id = 1L;

        Authors authors = new Authors();
        authors.setId(id);

        when(authorsRepository.findById(id)).thenReturn(Optional.of(authors));

        AuthorDTO authorDTO = authorService.getAuthorById(id);

        assertNotNull(authorDTO);
        assertEquals(id, authorDTO.getId());

        verify(authorsRepository, times(1)).findById(id);
    }

    @Test
    void getAuthorById_NotFound() {
        long id = 1L;
        when(authorsRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> authorService.getAuthorById(id));
        assertEquals("Author not found with id: " + id, exception.getMessage());
        verify(authorsRepository, times(1)).findById(id);
    }

    @Test
    void getAllAuthors_Success() {
        Authors authors = new Authors();
        authors.setId(1L);
        authors.setName("John Doe");

        Authors authors2 = new Authors();
        authors2.setId(2L);
        authors2.setName("Jane Doe");

        when(authorsRepository.findAll()).thenReturn(List.of(authors, authors2));
        List<AuthorDTO> authorDTOS = authorService.getAllAuthors();
        assertNotNull(authorDTOS);
        assertEquals(2, authorDTOS.size());
        verify(authorsRepository, times(1)).findAll();
    }

    @Test
    void updateAuthorById_Success() {
        long id = 1L;
        Authors authors = new Authors();
        authors.setId(id);
        authors.setName("John Doe");
        authors.setBookList(List.of(1L, 2L, 3L));

        Authors updatedAuthor = new Authors();
        updatedAuthor.setId(id);
        updatedAuthor.setName("Jane Doe");
        updatedAuthor.setBookList(List.of(1L, 2L, 3L));

        when(authorsRepository.findById(id)).thenReturn(Optional.of(authors));
        when(authorsRepository.save(any(Authors.class))).thenReturn(updatedAuthor);

        AuthorDTO authorDTO = authorService.updateAuthorById(id, AuthorDTO.fromEntity(updatedAuthor));
        assertNotNull(authorDTO);
        assertEquals("Jane Doe", authorDTO.getName());
        verify(authorsRepository, times(1)).save(any(Authors.class));
        verify(authorsRepository, times(1)).findById(id);
    }

    @Test
    void updateAuthorById_NotFound() {
        long id = 1L;

        Authors authors = new Authors();
        authors.setId(id);
        authors.setName("John Doe");
        authors.setBookList(List.of(1L, 2L, 3L));

        Authors updatedAuthor = new Authors();
        updatedAuthor.setId(id);
        updatedAuthor.setName("Jane Doe");
        updatedAuthor.setBookList(List.of(1L, 2L, 3L));

        when(authorsRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> authorService.updateAuthorById(id, AuthorDTO.fromEntity(updatedAuthor)));
        assertEquals("Author not found with id: " + id, exception.getMessage());
        verify(authorsRepository, times(1)).findById(id);
    }

    @Test
    void deleteAuthorById_Success() {

        long id = 1L;
        Authors authors = new Authors();
        authors.setId(id);

        doNothing().when(authorsRepository).deleteById(id);
        when(authorsRepository.existsById(id)).thenReturn(true);

        authorService.deleteAuthorById(id);

        verify(authorsRepository, times(1)).deleteById(id);

    }

    @Test
    void deleteAuthorById_NotFound() {
        long id = 1L;

        Exception exception = assertThrows(EntityNotFoundException.class, () -> authorService.deleteAuthorById(id));
        assertEquals("Author not found with id: " + id, exception.getMessage());
        verify(authorsRepository, never()).deleteById(id);
    }

    @Test
    void getAllAuthorsBooks_Success() {
        long authorId = 1L;

        when(authorsRepository.existsById(authorId)).thenReturn(true);

        List<Books> books = List.of(
                new Books(1L),
                new Books(2L),
                new Books(3L));
        List<Long> bookIds = List.of(1L, 2L, 3L);

        when(booksRepository.findByAuthorId(authorId)).thenReturn(books);

        List<Long> bookDTOS = authorService.getAllAuthorsBooks(authorId);
        assertNotNull(bookDTOS);
        assertEquals(bookIds, bookDTOS);

        verify(authorsRepository, times(1)).existsById(authorId);
        verify(booksRepository, times(1)).findByAuthorId(authorId);
    }

    @Test
    void getAllAuthorsBooks_NotFound() {
        long id = 1L;

        Exception exception = assertThrows(EntityNotFoundException.class, () -> authorService.getAuthorById(id));

        assertEquals("Author not found with id: " + id, exception.getMessage());
        verify(authorsRepository, times(1)).findById(id);
    }
}
