package com.example.testapp;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.enums.BookStatus;
import com.example.testapp.model.Books;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private BookService bookService;


    @Test
    void addBook() {

        Books books = new Books();
        books.setTitle("Book 1");
        books.setDescription("Nigger");
        books.setStatus(BookStatus.UNKNOWN);

        when(booksRepository.save(any(Books.class))).thenReturn(books);

        BookDTO savedBook = bookService.addBook(BookDTO.fromEntity(books));

        assertNotNull(savedBook);
        assertEquals("Book 1", savedBook.getTitle());
        assertEquals(BookStatus.UNKNOWN.name(), savedBook.getStatus());
        verify(booksRepository, times(1)).save(any(Books.class));
    }

    @Test
    void deleteBookByIdTest() {
        long id = 1L;

        bookService.deleteBookById(id);

        verify(booksRepository, times(1)).deleteById(id);
    }

    @Test
    void getBookByIdTest() {

        long id = 1L;

        Books book = new Books();
        book.setId(id);

        when(booksRepository.findById(id)).thenReturn(book);

         bookService.getBookById(id);

        verify(booksRepository, times(1)).findById(id);
    }

    @Test
    void updateBookByIdTest() {
        long id = 1L;

        Books existingBook = new Books();
        existingBook.setId(id);
        existingBook.setTitle("Old Title");

        Books updatedBook = new Books();
        updatedBook.setTitle("New Title");

        when(booksRepository.findById(id)).thenReturn(existingBook);
        when(booksRepository.save(existingBook)).thenReturn(existingBook);

        BookDTO bookDTO = bookService.updateBookById(id, BookDTO.fromEntity(updatedBook));
        verify(booksRepository, times(1)).findById(id);
        verify(booksRepository, times(1)).save(existingBook);

        assertEquals("New Title", bookDTO.getTitle());
    }

    @Test
    void getAllBooksTest() {

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
}
