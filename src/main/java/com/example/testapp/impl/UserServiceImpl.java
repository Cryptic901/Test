package com.example.testapp.impl;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.UserDTO;
import com.example.testapp.enums.UserRole;
import com.example.testapp.model.Book;
import com.example.testapp.model.Genre;
import com.example.testapp.model.User;
import com.example.testapp.repository.BookRepository;
import com.example.testapp.repository.GenreRepository;
import com.example.testapp.repository.UserRepository;
import com.example.testapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Cacheable(value = "users", key = "#username", unless = "#result == null")
    @Transactional
    public UserDTO getUserByUsername(String username) {
        return UserDTO.fromEntity(usersRepository.findUserByUsername(username)
                .orElse(null));
    }

    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    @Transactional
    public UserDTO getUserById(long id) {
        return UserDTO.fromEntity(usersRepository.findById(id)
                .orElse(null));
    }

    @CacheEvict(cacheNames = "users", key = "#id")
    @Transactional
    public UserDTO updateUserById(long id, UserDTO userDTO) {
        User user = usersRepository.findById(id)
                .orElse(null);
        if (user != null) {
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setRole(UserRole.valueOf(userDTO.getRole()));
            user.setBorrowedBook(userDTO.getBorrowedBook());
            return UserDTO.fromEntity(usersRepository.save(user));
        } else {
            return null;
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "users", key = "#userId"),
            @CacheEvict(value = "users", allEntries = true)
    })
    public String deleteUserById(long userId) {
        boolean exists = usersRepository.existsById(userId);
        if (!exists) {
            return "User not found";
        }
        UserDTO user = UserDTO.fromEntity(usersRepository.findById(userId)
                .orElse(null));
        if(user != null) {
            List<Long> borrowedBook = user.getBorrowedBook();
            if (borrowedBook != null && !borrowedBook.isEmpty()) {
                for (Long bookId : borrowedBook) {
                    returnBookById(bookId);
                }
            }
        }
        usersRepository.deleteById(userId);
        return "User deleted successfully";
    }

    @Transactional
    public String borrowBookById(long bookId) {
        long genreId = 0;
        Book book = booksRepository.findById(bookId)
                .orElse(null);

        if (book != null && book.getGenre() != null) {
            genreId = book.getGenre().getId();
            if (book.getQuantity() == 0) {
                return "We don't have that books";
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = usersRepository.findByEmail(username)
                .orElse(null);

        Genre genre = genresRepository.findById(genreId)
                .orElse(null);
        if (user != null && user.getBorrowedBook() == null) {
            user.setBorrowedBook(new ArrayList<>());
        }

        if (user != null && user.getBorrowedBook().size() > 5) {
            return "Too many borrowed books";
        }
        //Получение списка из аттрибутов пользователя и добавление книги в список
        if (book != null && genre != null && user != null) {
            List<Long> booksList = user.getBorrowedBook();
            booksList.add(bookId);

            //Изменение статуса книги
            Set<Long> borrowedUserBook = book.getBorrowedUserIds();
            borrowedUserBook.add(user.getId());
            book.setBorrowedUserIds(borrowedUserBook);
            book.setQuantity(book.getQuantity() - 1);
            book.setCountOfBorrowingBook(book.getCountOfBorrowingBook() + 1);
            genre.setCountOfBorrowingBookWithGenre(genre.getCountOfBorrowingBookWithGenre() + 1);

            //Сохранение в репозитории
            usersRepository.save(user);
            booksRepository.save(book);
            genresRepository.save(genre);
            return "Book borrowed successfully!";
        }
        return "User, Book or Genre not found";
    }


    @Transactional
    public String returnBookById(long bookId) {

        Book book = booksRepository.findById(bookId)
                .orElse(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = usersRepository.findByEmail(username)
                .orElse(null);
        if (user != null && book != null) {
            List<Long> booksList = user.getBorrowedBook();
            Set<Long> borrowedUserBook = book.getBorrowedUserIds();

            if (booksList == null ||!booksList.contains(bookId)) {
                return "Book is not borrowed!";
            }
            //Изменение статуса книги
            if (Collections.frequency(booksList, bookId) == 1) {
                borrowedUserBook.remove(user.getId());
            }
            // Удаляем книгу из списка пользователя, если он её вернул
            booksList.remove(book.getId());
            book.setQuantity(book.getQuantity() + 1);
            //Сохранение в репозитории
            usersRepository.save(user);
            booksRepository.save(book);
        }
        return "Book returned successfully!";
    }

    @CacheEvict(cacheNames = "users", key = "#id")
    @Transactional
    public UserDTO updateUserFields(long id, Map<String, Object> updates) {
        User user = usersRepository.findById(id)
                .orElse(null);
        if (user != null) {
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
        }
        return UserDTO.fromEntity(user);
    }

    public List<BookDTO> getBooksThatUserReadingById(long userId) {
        return booksRepository.findAll().stream()
                .filter(book -> book.getBorrowedUserIds().contains(userId))
                .map(BookDTO::fromEntity)
                .toList();
    }
}
