package com.example.testapp;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.enums.BookStatus;
import com.example.testapp.enums.UserRole;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private UserService userService;
    @Test
    void createUser_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@test.com");
        userDTO.setPassword("password");
        userDTO.setUserRole("USER");
        userDTO.setBorrowedBooks(new ArrayList<>());

        Users savedUser = new Users();
        savedUser.setId(1L);
        savedUser.setUsername("testUser");
        savedUser.setEmail("test@test.com");
        savedUser.setPassword("password");
        savedUser.setUserRole(UserRole.USER);
        savedUser.setBorrowedBooks(new ArrayList<>());

        when(usersRepository.save(any(Users.class))).thenReturn(savedUser);

        // Act
        UserDTO result = userService.createUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    void getAllUsers_Success() {
        // Arrange
        Users user1 = new Users();
        user1.setId(1L);
        user1.setUsername("user1");

        Users user2 = new Users();
        user2.setId(2L);
        user2.setUsername("user2");

        List<Users> usersList = Arrays.asList(user1, user2);
        when(usersRepository.findAll()).thenReturn(usersList);

        // Act
        List<UserDTO> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    void getUserByUsername_Success() {
        // Arrange
        String username = "testUser";
        Users user = new Users();
        user.setId(1L);
        user.setUsername(username);
        user.setEmail("test@test.com");

        when(usersRepository.findByUsername(username)).thenReturn(user);

        // Act
        UserDTO result = userService.getUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("test@test.com", result.getEmail());
        verify(usersRepository, times(1)).findByUsername(username);
    }

    @Test
    void findById_Success() {
        // Arrange
        long userId = 1L;
        Users user = new Users();
        user.setId(userId);
        user.setUsername("testUser");

        when(usersRepository.findById(userId)).thenReturn(user);

        // Act
        UserDTO result = userService.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(usersRepository, times(1)).findById(userId);
    }

    @Test
    void updateUserById_Success() {
        // Arrange
        long userId = 1L;
        UserDTO updateDTO = new UserDTO();
        updateDTO.setUsername("updatedUser");
        updateDTO.setEmail("updated@test.com");
        updateDTO.setUserRole("ADMIN");
        updateDTO.setBorrowedBooks(new ArrayList<>());

        Users existingUser = new Users();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");

        Users updatedUser = new Users();
        updatedUser.setId(userId);
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@test.com");
        updatedUser.setUserRole(UserRole.ADMIN);

        when(usersRepository.findById(userId)).thenReturn(existingUser);
        when(usersRepository.save(any(Users.class))).thenReturn(updatedUser);

        // Act
        UserDTO result = userService.updateUserById(userId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@test.com", result.getEmail());
        assertEquals("ADMIN", result.getUserRole());
        verify(usersRepository, times(1)).findById(userId);
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    void deleteUserById_Success() {
        // Arrange
        long userId = 1L;
        doNothing().when(usersRepository).deleteById(userId);

        // Act
        userService.deleteUserById(userId);

        // Assert
        verify(usersRepository, times(1)).deleteById(userId);
    }

    @Test
    void getUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentUser";
        when(usersRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        assertNull(userService.getUserByUsername(username));
        verify(usersRepository, times(1)).findByUsername(username);
    }

    @Test
    void updateUserById_UserNotFound() {
        // Arrange
        long userId = 999L;
        UserDTO updateDTO = new UserDTO();
        when(usersRepository.findById(userId)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.updateUserById(userId, updateDTO));
        verify(usersRepository, times(1)).findById(userId);
        verify(usersRepository, never()).save(any(Users.class));
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
        book.setStatus(BookStatus.AVAILABLE);

        when(booksRepository.findById(bookId)).thenReturn(book);
        when(usersRepository.findById(userId)).thenReturn(user);
        when(booksRepository.save(any(Books.class))).thenReturn(book);

        String result = userService.borrowBookById(bookId, userId);

        assertEquals("Book borrowed successfully!", result);
        assertEquals(user, book.getUser());
        assertEquals(BookStatus.BORROWED, book.getStatus());

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
        book.setStatus(BookStatus.BORROWED);

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
        user.setBorrowedBooks(new ArrayList<>());

        Books book = new Books();
        book.setId(bookId);
        book.setTitle("Book 1");
        book.setUser(user);
        book.setStatus(BookStatus.BORROWED);

        doReturn(user).when(usersRepository).findById(userId);

        doReturn(book).when(booksRepository).findById(anyLong());
        doReturn(book).when(booksRepository).save(any(Books.class));

        String result = userService.returnBookById(bookId, userId);

        assertEquals("Book returned successfully!", result);
        assertNull(book.getUser());
        assertEquals(BookStatus.AVAILABLE, book.getStatus());

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
        book.setStatus(BookStatus.AVAILABLE);

        doReturn(book).when(booksRepository).findById(anyLong());

        Exception exception = assertThrows(RuntimeException.class,
                () -> userService.returnBookById(userId, bookId));
        assertEquals("Book is not borrowed!", exception.getMessage());

        verify(booksRepository, never()).save(any(Books.class));
    }
}
