package com.example.testapp.service.impl;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.enums.UserRole;
import com.example.testapp.exceptions.EntityNotFoundException;
import com.example.testapp.model.Book;
import com.example.testapp.model.Genre;
import com.example.testapp.model.User;
import com.example.testapp.repository.BookRepository;
import com.example.testapp.repository.GenreRepository;
import com.example.testapp.repository.UserRepository;
import com.example.testapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/* Сервис для обработки пользовательских данных */

@Service
public class UserServiceImpl implements UserService {

    private final BookRepository booksRepository;
    private final UserRepository usersRepository;
    private final GenreRepository genresRepository;

    @Autowired
    public UserServiceImpl(BookRepository booksRepository, UserRepository usersRepository, GenreRepository genresRepository) {
        this.booksRepository = booksRepository;
        this.usersRepository = usersRepository;
        this.genresRepository = genresRepository;
    }

    public List<UserDTO> getAllUser() {
        List<User> users = usersRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(UserDTO.fromEntity(user));
        }
        return userDTOs;
    }

    public UserDTO getUserByUsername(String username) {
        return UserDTO.fromEntity(usersRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username)));
    }
    public UserDTO getUserById(long id) {
        return UserDTO.fromEntity(usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id)));
    }

    public UserDTO updateUserById(long id, UserDTO userDTO) {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(UserRole.valueOf(userDTO.getRole()));
        user.setBorrowedBook(userDTO.getBorrowedBook());
        return UserDTO.fromEntity(usersRepository.save(user));
    }

    @Transactional
    public void deleteUserById(long userId) {
        if (!usersRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        UserDTO user = getUserById(userId);
        List<Long> borrowedBook = user.getBorrowedBook();
        if (borrowedBook != null && !borrowedBook.isEmpty()) {
            for (Long bookId : borrowedBook) {
                returnBookById(bookId, userId);
            }
        }
        usersRepository.deleteById(userId);
    }

    @Transactional
    public String borrowBookById(long bookId, long userId) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found or has reached the maximum of borrowed books with id: " + userId));

        Genre genre = genresRepository.findById(book.getGenre().getId())
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + book.getGenre().getId()));
        if (user.getBorrowedBook() == null) {
            user.setBorrowedBook(new ArrayList<>());
        }

        if (user.getBorrowedBook().size() > 5) {
            throw new RuntimeException("Too many borrowed books");
        }
        //Получение списка из аттрибутов пользователя и добавление книги в список
        List<Long> booksList = user.getBorrowedBook();
        booksList.add(book.getId());

        //Изменение статуса книги
        Set<Long> borrowedUserBook = book.getBorrowedUserIds();
        borrowedUserBook.add(userId);
        book.setBorrowedUserIds(borrowedUserBook);
        book.setAmount(book.getAmount() - 1);
        book.setCountOfBorrowingBook(book.getCountOfBorrowingBook() + 1);
        genre.setCountOfBorrowingBookWithGenre(genre.getCountOfBorrowingBookWithGenre() + 1);

        //Сохранение в репозитории
        usersRepository.save(user);
        booksRepository.save(book);
        genresRepository.save(genre);

        return "Book borrowed successfully!";
    }


    @Transactional
    public String returnBookById(long bookId, long userId) {

        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found or not borrow a book with id: " + userId));

        List<Long> booksList = user.getBorrowedBook();
        Set<Long> borrowedUserBook = book.getBorrowedUserIds();

        if (!booksList.contains(bookId)) {
            throw new RuntimeException("Book is not borrowed!");
        }
        //Изменение статуса книги
        if (Collections.frequency(booksList, bookId) == 1) {
            borrowedUserBook.remove(userId);
        }
        // Удаляем книгу из списка пользователя, если он её вернул
        booksList.remove(book.getId());
        book.setAmount(book.getAmount() + 1);
        //Сохранение в репозитории
        usersRepository.save(user);
        booksRepository.save(book);

        return "Book returned successfully!";
    }

    public UserDTO updateUserFields(long id, Map<String, Object> updates) {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (updates.containsKey("username")) {
            user.setUsername((String) updates.get("username"));
        }
        if (updates.containsKey("password")) {
            user.setPassword((String) updates.get("password"));
        }
        if (updates.containsKey("email")) {
            user.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("role")) {
            user.setRole(UserRole.valueOf((String) updates.get("role")));
        }
        usersRepository.save(user);
        return UserDTO.fromEntity(user);
    }
}
