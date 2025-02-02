package com.example.testapp;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.model.Books;
import com.example.testapp.model.Users;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.UsersRepository;
import com.example.testapp.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.print.Book;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BooksRepository booksRepository;
    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private BookService bookService;


    @Test
    void addBook() {

        Books books = new Books();
        books.setTitle("Book 1");
        books.setDescription("Nigger");
        books.setStatus(Books.BookStatus.UNKNOWN);

        when(booksRepository.save(any(Books.class))).thenReturn(books);

        BookDTO savedBook = bookService.addBook(BookDTO.fromEntity(books));

        assertNotNull(savedBook);
        assertEquals("Book 1", savedBook.getTitle());
        assertEquals(Books.BookStatus.UNKNOWN.name(), savedBook.getStatus());
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

        Books books = bookService.updateBookById(id, updatedBook);
        verify(booksRepository, times(1)).findById(id);
        verify(booksRepository, times(1)).save(existingBook);

        assertEquals("New Title", books.getTitle());
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

    @Test
    void borrowBookById_Success() {
        long userId = 1L;
        long bookId = 10L;

        Users user = new Users();
        user.setId(userId);
        user.setUsername("username");

        Books book = new Books();
        book.setId(bookId);
        book.setTitle("Book 1");
        book.setUser(null);

        when(booksRepository.findById(bookId)).thenReturn(book);
        when(usersRepository.findById(userId)).thenReturn(user);
        when(booksRepository.save(any(Books.class))).thenReturn(book);

        String result = bookService.borrowBookById(bookId, userId);

        assertEquals("Book borrowed successfully!", result);
        assertEquals(user, book.getUser());

        verify(booksRepository, times(1)).save(book);
    }

    @Test
    void borrowBookById_AlreadyBorrowed() {
        long userId = 1L;
        long bookId = 10L;

        Users user1 = new Users();
        user1.setId(userId);
        user1.setUsername("username1");

        Users user2 = new Users();
        user2.setId(2L);
        user2.setUsername("username2");

        Books book = new Books();
        book.setId(bookId);
        book.setTitle("Book 1");
        book.setUser(user2);

        when(booksRepository.findById(bookId)).thenReturn(book);
        when(usersRepository.findById(userId)).thenReturn(user1);

        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.borrowBookById(bookId, userId));
        assertEquals("Book is already borrowed!", exception.getMessage());

        verify(booksRepository, never()).save(any(Books.class));
    }

    @Test
    void returnBookById_Success() {
        long userId = 1L;
        long bookId = 10L;

        Users user = new Users();
        user.setId(userId);
        user.setUsername("username");

        Books book = new Books();
        book.setId(bookId);
        book.setTitle("Book 1");
        book.setUser(user);

        doReturn(book).when(booksRepository).findById(anyLong());
        doReturn(book).when(booksRepository).save(any(Books.class));

        String result = bookService.returnBookById(userId, bookId);

        assertEquals("Book returned successfully!", result);
        assertNull(book.getUser());

        verify(booksRepository, times(1)).save(book);
    }

    @Test
    void returnBookById_AlreadyReturned() {
        long userId = 1L;
        long bookId = 10L;

        Users user = new Users();
        user.setId(userId);
        user.setUsername("username");

        Books book = new Books();
        book.setId(bookId);
        book.setTitle("Book 1");
        book.setUser(null);

        doReturn(book).when(booksRepository).findById(anyLong());

        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.returnBookById(userId, bookId));
        assertEquals("Book is not borrowed!", exception.getMessage());

        verify(booksRepository, never()).save(any(Books.class));
    }
}
