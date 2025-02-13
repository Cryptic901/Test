package com.example.testapp.service;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.enums.BookStatus;
import com.example.testapp.enums.UserRole;
import com.example.testapp.model.Books;
import com.example.testapp.model.Users;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/* Сервис для обработки пользовательских данных */

@Service
public class UserService {

    private final BooksRepository booksRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public UserService(BooksRepository booksRepository, UsersRepository usersRepository) {
        this.booksRepository = booksRepository;
        this.usersRepository = usersRepository;
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
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username)));
    }

    public UserDTO getUserById(long id) {
        return UserDTO.fromEntity(usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id)));
    }

    public UserDTO updateUserById(long id, UserDTO userDTO) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        setUserParams(user, userDTO);
        return UserDTO.fromEntity(usersRepository.save(user));
    }

    @Transactional
    public void deleteUserById(long userId) {
        UserDTO user = getUserById(userId);
        if (user == null) {
            return;
        }
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
                .orElseThrow(() -> new RuntimeException("Book not found or already borrowed with id: " + bookId));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found or has reached the maximum of borrowed books with id: " + userId));

        //Обработка если книга уже занята
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is already borrowed!");
        }

        if (user.getBorrowedBooks() == null) {
            user.setBorrowedBooks(new ArrayList<>());
        }

        //Получение списка из аттрибутов пользователя и добавление книги в список
        List<Long> booksList = user.getBorrowedBooks();
        booksList.add(book.getId());

        //Изменение статуса книги
        book.setUser(user);
        book.setStatus(BookStatus.BORROWED);
        book.setAmount(book.getAmount() - 1);

        //Сохранение в репозитории
        usersRepository.save(user);
        booksRepository.save(book);

        return "Book borrowed successfully!";
    }


    @Transactional
    public String returnBookById(long bookId, long userId) {

        Books book = booksRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found or not borrow a book with id: " + userId));

        //Обработка если книга не занята
        if (book.getStatus() != BookStatus.BORROWED) {
            throw new RuntimeException("Book is not borrowed!");
        }

        if (!book.getUser().getId().equals(userId)) {
            throw new RuntimeException("Book borrowed by another user!");
        }

        // Удаляем книгу из списка пользователя, если он её вернул
        if (user.getBorrowedBooks() != null) {
            user.getBorrowedBooks().remove(book.getId());
        }

        //Получение списка из аттрибутов пользователя и удаление книги из списка
        List<Long> booksList = user.getBorrowedBooks();
        booksList.remove(book.getId());

        //Изменение статуса книги
        book.setUser(null);
        book.setStatus(BookStatus.AVAILABLE);
        book.setAmount(book.getAmount() + 1);
        //Сохранение в репозитории
        usersRepository.save(user);
        booksRepository.save(book);

        return "Book returned successfully!";
    }
}
