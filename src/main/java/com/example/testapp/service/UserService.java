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
        return UserDTO.fromEntity(usersRepository.findByUsername(username));
    }

    public UserDTO getUserById(long id) {
        return UserDTO.fromEntity(usersRepository.findById(id));
    }

    public UserDTO updateUserById(long id, UserDTO userDTO) {
        Users user = usersRepository.findById(id);
        setUserParams(user, userDTO);
        return UserDTO.fromEntity(usersRepository.save(user));
    }

    public void deleteUserById(long id) {
        usersRepository.deleteById(id);
    }

    @Transactional
    public String borrowBookById(long bookId, long userId) {

        Users user = usersRepository.findById(userId);
        Books book = booksRepository.findById(bookId);

        //Обработка если книга уже занята
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is already borrowed!");
        }

        if (user.getBorrowedBooks() == null) {
            user.setBorrowedBooks(new ArrayList<>());
        }

        //Получение списка из аттрибутов пользователя и добавление книги в список
        List<Books> booksList = user.getBorrowedBooks();
        booksList.add(book);

        //Изменение статуса книги
        book.setUser(user);
        book.setStatus(BookStatus.BORROWED);

        //Сохранение в репозитории
        usersRepository.save(user);
        booksRepository.save(book);

        return "Book borrowed successfully!";
    }


    @Transactional
    public String returnBookById(long bookId, long userId) {

        Books book = booksRepository.findById(bookId);
        Users user = usersRepository.findById(userId);

        //Обработка если книга не занята
        if (book.getStatus() != BookStatus.BORROWED || book.getUser().getId() != userId) {
            throw new RuntimeException("Book is not borrowed!");
        }

        // Удаляем книгу из списка пользователя, если он её вернул
        if (user.getBorrowedBooks() != null) {
            user.getBorrowedBooks().remove(book);
        }

        //Получение списка из аттрибутов пользователя и удаление книги из списка
        List<Books> booksList = user.getBorrowedBooks();
        booksList.remove(book);

        //Изменение статуса книги
        book.setUser(null);
        book.setStatus(BookStatus.AVAILABLE);

        //Сохранение в репозитории
        usersRepository.save(user);
        booksRepository.save(book);

        return "Book returned successfully!";
    }
}
