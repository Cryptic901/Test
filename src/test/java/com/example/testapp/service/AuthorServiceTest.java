package com.example.testapp.service;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
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

        long bookId = 1L;
        long authorId = 1L;
        Authors authors = new Authors();
        authors.setId(authorId);
        authors.setBookList(List.of(1L));
        Books book = new Books();
        book.setId(bookId);

        doNothing().when(authorsRepository).deleteById(bookId);
        when(authorsRepository.existsById(bookId)).thenReturn(true);
        when(authorsRepository.findById(bookId)).thenReturn(Optional.of(authors));
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));

        authorService.deleteAuthorById(bookId);

        verify(authorsRepository, times(1)).deleteById(bookId);
        verify(authorsRepository, times(1)).existsById(bookId);
        verify(authorsRepository, times(1)).findById(bookId);
        verify(booksRepository, times(1)).findById(bookId);

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

        List<Books> booksList = List.of(
                new Books(1L, "Book Title 1"),
                new Books(2L, "Book Title 2"),
                new Books(3L, "Book Title 3")
        );

        List<BookShortDTO> expectedDtoList = List.of(
                new BookShortDTO(1L, "Book Title 1"),
                new BookShortDTO(2L, "Book Title 2"),
                new BookShortDTO(3L, "Book Title 3")
        );

        when(authorsRepository.existsById(authorId)).thenReturn(true);
        when(booksRepository.findByAuthorId(authorId)).thenReturn(booksList);

        List<BookShortDTO> result = authorService.getAllAuthorsBooks(authorId);
        assertNotNull(result);
        assertEquals(expectedDtoList, result);

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
