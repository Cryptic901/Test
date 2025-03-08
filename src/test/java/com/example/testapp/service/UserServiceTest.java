package com.example.testapp.service;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.enums.UserRole;
import com.example.testapp.model.Book;
import com.example.testapp.model.Genre;
import com.example.testapp.model.User;
import com.example.testapp.repository.BookRepository;
import com.example.testapp.repository.GenreRepository;
import com.example.testapp.repository.UserRepository;
import com.example.testapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository usersRepository;

    @Mock
    private BookRepository booksRepository;

    @Mock
    private GenreRepository genresRepository;

    @Spy
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUser_Success() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        List<User> usersList = Arrays.asList(user1, user2);
        when(usersRepository.findAll()).thenReturn(usersList);

        // Act
        List<UserDTO> result = userService.getAllUser();

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
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setEmail("test@test.com");

        when(usersRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDTO result = userService.getUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("test@test.com", result.getEmail());
        verify(usersRepository, times(1)).findUserByUsername(username);
    }

    @Test
    void getUserById_Success() {
        // Arrange
        long userId = 1L;
        User user = new User();
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
        updateDTO.setRole("ADMIN");
        updateDTO.setBorrowedBook(new ArrayList<>());

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@test.com");
        updatedUser.setRole(UserRole.ADMIN);

        when(usersRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(usersRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        UserDTO result = userService.updateUserById(userId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@test.com", result.getEmail());
        assertEquals("ADMIN", result.getRole());
        verify(usersRepository, times(1)).findById(userId);
        verify(usersRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUserById_Success() {
        // Arrange
        long userId = 1L;

        // Создаем DTO которое будет возвращать getUserById
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setBorrowedBook(new ArrayList<>());

        // Мокаем только необходимые методы
        doReturn(userDTO).when(userService).getUserById(userId);
        doNothing().when(usersRepository).deleteById(userId);
        when(usersRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteUserById(userId);

        // Assert
        verify(usersRepository, times(1)).deleteById(userId);
    }

    @Test
    void getUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentUser";
        when(usersRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> userService.getUserByUsername(username));
        // Assert
        assertEquals("User not found with username: " + username, exception.getMessage());
        verify(usersRepository, times(1)).findUserByUsername(username);
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
        verify(usersRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUserById_WithBook_Success() {
        // Arrange
        long userId = 1L;
        long bookId1 = 1L;
        long bookId2 = 2L;
        // Создаем пользователя
        User user = new User();
        user.setId(userId);
        // Создаем DTO с книгами
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setBorrowedBook(new ArrayList<>(List.of(bookId1, bookId2)));
        // Создаем книги и устанавливаем им пользователя
        Book book1 = new Book();
        book1.setId(bookId1);
        book1.setBorrowedUserIds(new HashSet<>(Set.of(userId)));  // Устанавливаем связь с пользователем
        Book book2 = new Book();
        book2.setId(bookId2);
        book2.setBorrowedUserIds(new HashSet<>(Set.of(userId)));  // Устанавливаем связь с пользователем
        user.setBorrowedBook(List.of(bookId1,bookId2));

        // Мокаем getUserById
        doReturn(userDTO).when(userService).getUserById(userId);
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.existsById(userId)).thenReturn(true);
        // Мокаем findById для книг
        when(booksRepository.findById(bookId1)).thenReturn(Optional.of(book1));
        when(booksRepository.findById(bookId2)).thenReturn(Optional.of(book2));
        doNothing().when(usersRepository).deleteById(userId);
        // Мокаем сохранение книг
        when(booksRepository.save(any(Book.class))).thenReturn(book1);
        // Act
        userService.deleteUserById(userId);

        // Assert
        verify(usersRepository, times(1)).deleteById(userId);
        verify(booksRepository, times(1)).findById(bookId1);
        verify(booksRepository, times(1)).findById(bookId2);
    }

    @Test
    void borrowBookById_Success() {
        long userId = 1L;
        long bookId = 10L;
        long genreId = 2L;
        Genre genre = new Genre();
        genre.setId(genreId);
        User user = new User();
        user.setId(userId);
        user.setUsername("username");
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Book 1");
        book.setGenre(genre);
        book.setBorrowedUserIds(new HashSet<>(Set.of(userId)));

        when(booksRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(booksRepository.save(any(Book.class))).thenReturn(book);
        when(genresRepository.findById(book.getGenre().getId())).thenReturn(Optional.of(genre));
        String result = userService.borrowBookById(bookId, userId);

        assertEquals("Book borrowed successfully!", result);
        assertEquals(Optional.of(userId), book.getBorrowedUserIds().stream().findFirst());
        verify(booksRepository, times(1)).save(book);
    }

    @Test
    void returnBookById_Success() {
        long userId = 1L;
        long bookId = 10L;
        User user = new User();
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Book 1");
        book.setBorrowedUserIds(new HashSet<>(Set.of(userId)));
        user.setId(userId);
        user.setUsername("username");
        user.setBorrowedBook(List.of(bookId));

        doReturn(Optional.of(user)).when(usersRepository).findById(userId);
        doReturn(Optional.of(book)).when(booksRepository).findById(bookId);
        doReturn(book).when(booksRepository).save(any(Book.class));
        doReturn(user).when(usersRepository).save(any(User.class));
        String result = userService.returnBookById(bookId, userId);

        assertEquals("Book returned successfully!", result);
        assertEquals(book.getBorrowedUserIds(), Collections.emptySet());
        verify(booksRepository, times(1)).save(book);
        verify(booksRepository, times(1)).findById(bookId);
        verify(usersRepository, times(1)).findById(userId);
    }

    @Test
    void returnBookById_AlreadyReturned() {

        long userId = 1L;
        long bookId = 10L;

        User user = new User();
        user.setId(userId);
        user.setUsername("username");

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Book 1");
        book.setBorrowedUserIds(null);

        doReturn(Optional.of(book)).when(booksRepository).findById(bookId);
        doReturn(Optional.of(user)).when(usersRepository).findById(userId);

        Exception exception = assertThrows(RuntimeException.class,
                () -> userService.returnBookById(bookId, userId));
        assertEquals("Book is not borrowed!", exception.getMessage());

        verify(booksRepository, never()).save(any(Book.class));
    }
}
