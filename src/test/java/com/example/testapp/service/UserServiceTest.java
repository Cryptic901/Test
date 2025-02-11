package com.example.testapp.service;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.enums.BookStatus;
import com.example.testapp.enums.UserRole;
import com.example.testapp.model.Books;
import com.example.testapp.model.Users;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        UserDTO userDTO = new UserDTO(new Users(1L, "testUser", "test@test.com", "password", UserRole.USER, new ArrayList<>()));
        Users savedUser = new Users(1L, "testUser", "test@test.com", "password", UserRole.USER, new ArrayList<>());

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

        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));

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

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UserDTO result = userService.getUserById(userId);

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

        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));
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

        // Создаем DTO которое будет возвращать getUserById
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setBorrowedBooks(new ArrayList<>());

        // Мокаем только необходимые методы
        UserService spyUserService = spy(userService);
        doReturn(userDTO).when(spyUserService).getUserById(userId);
        doNothing().when(usersRepository).deleteById(userId);

        // Act
        spyUserService.deleteUserById(userId);

        // Assert
        verify(usersRepository, times(1)).deleteById(userId);
    }

    @Test
    void getUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentUser";
        when(usersRepository.findByUsername(username)).thenReturn(null);

        // Act
        UserDTO result = userService.getUserByUsername(username);
        // Assert
        assertNull(result);
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
    void deleteUserById_WithBooks_Success() {
        // Arrange
        long userId = 1L;
        long bookId1 = 1L;
        long bookId2 = 2L;

        // Создаем пользователя
        Users user = new Users();
        user.setId(userId);
        // Создаем DTO с книгами
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setBorrowedBooks(Arrays.asList(bookId1, bookId2));

        // Создаем книги и устанавливаем им пользователя
        Books book1 = new Books();
        book1.setId(bookId1);
        book1.setStatus(BookStatus.BORROWED);
        book1.setUser(user);  // Устанавливаем связь с пользователем

        Books book2 = new Books();
        book2.setId(bookId2);
        book2.setStatus(BookStatus.BORROWED);
        book2.setUser(user);  // Устанавливаем связь с пользователем
        user.setBorrowedBooks(Arrays.asList(book1.getId(), book2.getId()));

        UserService spyUserService = spy(userService);

        // Мокаем getUserById
        doReturn(userDTO).when(spyUserService).getUserById(userId);

        // Мокаем findById для книг
        when(booksRepository.findById(bookId1)).thenReturn(Optional.of(book1));
        when(booksRepository.findById(bookId2)).thenReturn(Optional.of(book2));
        doNothing().when(usersRepository).deleteById(userId);

        // Мокаем сохранение книг
        when(booksRepository.save(any(Books.class))).thenReturn(book1);

        // Act
        spyUserService.deleteUserById(userId);

        // Assert
        verify(usersRepository, times(1)).deleteById(userId);
        verify(booksRepository, times(1)).findById(bookId1);
        verify(booksRepository, times(1)).findById(bookId2);
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

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
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

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user1));

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
