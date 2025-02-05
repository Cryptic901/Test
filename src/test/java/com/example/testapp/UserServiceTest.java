package com.example.testapp;

import com.example.testapp.enums.BookStatus;
import com.example.testapp.model.Books;
import com.example.testapp.model.Users;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.UsersRepository;
import com.example.testapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


//TODO
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private UserService userService;

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
        book.setStatus(BookStatus.valueOf("AVAILABLE"));

        when(booksRepository.findById(bookId)).thenReturn(book);
        when(usersRepository.findById(userId)).thenReturn(user);
        when(booksRepository.save(any(Books.class))).thenReturn(book);

        String result = userService.borrowBookById(bookId, userId);

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
                () -> userService.borrowBookById(bookId, userId));
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

        String result = userService.returnBookById(userId, bookId);

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
                () -> userService.returnBookById(userId, bookId));
        assertEquals("Book is not borrowed!", exception.getMessage());

        verify(booksRepository, never()).save(any(Books.class));
    }
}
