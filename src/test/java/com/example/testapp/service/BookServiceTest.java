package com.example.testapp.service;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.model.Authors;
import com.example.testapp.model.Books;
import com.example.testapp.repository.AuthorsRepository;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.GenresRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BooksRepository booksRepository;

    @Mock
    private AuthorsRepository authorsRepository;

    @Mock
    private GenresRepository genresRepository;

    @InjectMocks
    private BookService bookService;


    @Test
    void addBook_Success() {
       //TODO
    }

    @Test
    void deleteBookByIdTest_Success() {
        long bookId = 1L;
        long authorId = 1L;
        Authors authors = new Authors(authorId);
        authors.setId(authorId);
        authors.setBookList(new ArrayList<>(List.of(bookId)));
        Books books = new Books();
        books.setId(bookId);
        books.setAuthor(authors);

        doNothing().when(booksRepository).deleteById(bookId);
        when(booksRepository.existsById(bookId)).thenReturn(true);
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(books));
        when(authorsRepository.findById(books.getAuthor().getId())).thenReturn(Optional.of(authors));

        bookService.deleteBookById(bookId);

        verify(booksRepository, times(1)).deleteById(bookId);
    }

    @Test
    void deleteBookByIdTest_NotFound() {
        long id = 1L;

        Exception exception = assertThrows(RuntimeException.class, () -> bookService.deleteBookById(id));

        assertEquals("Book not found with id " + id, exception.getMessage());

        verify(booksRepository, never()).deleteById(id);
    }

    @Test
    void getBookByIdTest_Success() {

        long id = 1L;

        Books book = new Books();
        book.setId(id);

        when(booksRepository.findById(id)).thenReturn(Optional.of(book));

        bookService.getBookById(id);

        verify(booksRepository, times(1)).findById(id);
    }

    @Test
    void getBookByIdTest_NotFound() {

        long id = 1L;

        when(booksRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> bookService.getBookById(id));
        assertEquals("Book not found with id: " + id, exception.getMessage());

        verify(booksRepository, times(1)).findById(id);
    }

    @Test
    void updateBookById_Success() {
        long id = 1L;

        Books existingBook = new Books();
        existingBook.setId(id);
        existingBook.setTitle("Old Title");

        Books updatedBook = new Books();
        updatedBook.setTitle("New Title");

        when(booksRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(booksRepository.save(existingBook)).thenReturn(existingBook);

        BookDTO bookDTO = bookService.updateBookById(id, BookDTO.fromEntity(updatedBook));
        verify(booksRepository, times(1)).findById(id);
        verify(booksRepository, times(1)).save(existingBook);

        assertEquals("New Title", bookDTO.getTitle());
    }

    @Test
    void getAllBooks_Success() {

        Books book1 = new Books();
        book1.setId(1L);
        book1.setTitle("Book 1");
        Books book2 = new Books();
        book2.setId(2L);
        book2.setTitle("Book 2");

        List<Books> mockList = List.of(book1, book2);

        when(booksRepository.findAll()).thenReturn(mockList);

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(mockList.size(), result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());

        verify(booksRepository, times(1)).findAll();
    }

    @Test
    void getBookByIsbn_Success() {

        String isbn = "123";

        Books book = new Books();
        book.setIsbn(isbn);

        when(booksRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        bookService.getBookByIsbn(isbn);

        verify(booksRepository, times(1)).findByIsbn(isbn);
    }

    @Test
    void getBookByIsbn_NotFound() {

        String isbn = "123";

        when(booksRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> bookService.getBookByIsbn(isbn));
        assertEquals("Book not found with isbn: " + isbn, exception.getMessage());

        verify(booksRepository, times(1)).findByIsbn(isbn);
    }
}
