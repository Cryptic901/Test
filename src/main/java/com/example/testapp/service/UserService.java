package com.example.testapp.service;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.enums.UserRole;
import com.example.testapp.exceptions.EntityNotFoundException;
import com.example.testapp.model.Books;
import com.example.testapp.model.Genres;
import com.example.testapp.model.Users;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.GenresRepository;
import com.example.testapp.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/* Сервис для обработки пользовательских данных */

@Service
public class UserService {

    private final BooksRepository booksRepository;
    private final UsersRepository usersRepository;
    private final GenresRepository genresRepository;

    @Autowired
    public UserService(BooksRepository booksRepository, UsersRepository usersRepository, GenresRepository genresRepository) {
        this.booksRepository = booksRepository;
        this.usersRepository = usersRepository;
        this.genresRepository = genresRepository;
    }

    public void setUserParams(Users user, UserDTO userDTO) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setUserRole(UserRole.valueOf(userDTO.getUserRole()));
        user.setBorrowedBooks(userDTO.getBorrowedBooks());
    }


    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.getUsername() == null || userDTO.getEmail() == null || userDTO.getPassword() == null) {
            throw new IllegalArgumentException("Parameters are null");
        }
        Users user = new Users();
        setUserParams(user, userDTO);
        return UserDTO.fromEntity(usersRepository.save(user));
    }

    public List<UserDTO> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (Users user : users) {
            userDTOs.add(UserDTO.fromEntity(user));
        }
        return userDTOs;
    }

    public UserDTO getUserByUsername(String username) {
        return UserDTO.fromEntity(usersRepository.findUsersByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username)));
    }

    public UserDTO getUserById(long id) {
        return UserDTO.fromEntity(usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id)));
    }

    public UserDTO updateUserById(long id, UserDTO userDTO) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        setUserParams(user, userDTO);
        return UserDTO.fromEntity(usersRepository.save(user));
    }

    @Transactional
    public void deleteUserById(long userId) {
        if (!usersRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        UserDTO user = getUserById(userId);
        List<Long> borrowedBooks = user.getBorrowedBooks();
        if (borrowedBooks != null && !borrowedBooks.isEmpty()) {
            for (Long bookId : borrowedBooks) {
                returnBookById(bookId, userId);
            }
        }
        usersRepository.deleteById(userId);
    }

    @Transactional
    public String borrowBookById(long bookId, long userId) {
        Books book = booksRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found or has reached the maximum of borrowed books with id: " + userId));

        Genres genre = genresRepository.findById(book.getGenre().getId())
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + book.getGenre().getId()));
        if (user.getBorrowedBooks() == null) {
            user.setBorrowedBooks(new ArrayList<>());
        }

        if (user.getBorrowedBooks().size() > 5) {
            throw new RuntimeException("Too many borrowed books");
        }
        //Получение списка из аттрибутов пользователя и добавление книги в список
        List<Long> booksList = user.getBorrowedBooks();
        booksList.add(book.getId());

        //Изменение статуса книги
        Set<Long> borrowedUserBooks = book.getBorrowedUserIds();
        borrowedUserBooks.add(userId);
        book.setBorrowedUserIds(borrowedUserBooks);
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

        Books book = booksRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found or not borrow a book with id: " + userId));

        List<Long> booksList = user.getBorrowedBooks();
        Set<Long> borrowedUserBooks = book.getBorrowedUserIds();

        if (!booksList.contains(bookId)) {
            throw new RuntimeException("Book is not borrowed!");
        }
        //Изменение статуса книги
        if (Collections.frequency(booksList, bookId) == 1) {
            borrowedUserBooks.remove(userId);
        }
        // Удаляем книгу из списка пользователя, если он её вернул
        booksList.remove(book.getId());
        book.setAmount(book.getAmount() + 1);
        //Сохранение в репозитории
        usersRepository.save(user);
        booksRepository.save(book);

        return "Book returned successfully!";
    }
}
