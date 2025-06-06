package com.example.testapp;

import com.example.testapp.enums.UserRole;
import com.example.testapp.model.Author;
import com.example.testapp.model.Book;
import com.example.testapp.model.Genre;
import com.example.testapp.model.User;
import com.example.testapp.repository.AuthorRepository;
import com.example.testapp.repository.BookRepository;
import com.example.testapp.repository.GenreRepository;
import com.example.testapp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@AutoConfigureDataRedis
public abstract class SetUpForIntegrationTests {

    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected GenreRepository genreRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CacheManager cacheManager;

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    protected final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUpTestData() {
        Objects.requireNonNull(cacheManager.getCache("users")).clear();
        Objects.requireNonNull(cacheManager.getCache("books")).clear();
        Objects.requireNonNull(cacheManager.getCache("genres")).clear();
        Objects.requireNonNull(cacheManager.getCache("authors")).clear();
        userRepository.deleteAll();
        userRepository.flush();
        bookRepository.deleteAll();
        bookRepository.flush();
        authorRepository.deleteAll();
        authorRepository.flush();
        genreRepository.deleteAll();
        genreRepository.flush();

        Author author1 = new Author("author1");
        List<Long> authors = new ArrayList<>();
        authors.add(1L);
        authors.add(2L);
        author1.setBookList(authors);
        authorRepository.saveAndFlush(author1);

        Author author2 = new Author("author2");
        List<Long> list = new ArrayList<>();
        list.add(3L);
        author2.setBookList(list);
        authorRepository.saveAndFlush(author2);

        Author author3 = new Author("author3");
        authorRepository.saveAndFlush(author3);

        Genre genre1 = new Genre("genre1", "genre desc1");
        genre1.setCountOfBorrowingBookWithGenre(1L);
        List<Long> genres = new ArrayList<>();
        genres.add(25L);
        genres.add(26L);
        genre1.setBooks(genres);
        genreRepository.saveAndFlush(genre1);

        Genre genre2 = new Genre("genre2", "genre desc2");
        genre2.setCountOfBorrowingBookWithGenre(6L);
        List<Long> genres2 = new ArrayList<>();
        genres2.add(3L);
        genre2.setBooks(genres2);
        genreRepository.saveAndFlush(genre2);

        Genre genre3 = new Genre("genre3", "genre desc3");
        genre3.setCountOfBorrowingBookWithGenre(3L);
        List<Long> genres3 = new ArrayList<>();
        genres3.add(28L);
        genre3.setBooks(genres3);
        genreRepository.saveAndFlush(genre3);

        Book book1 = new Book("Test Book", "1234567890", LocalDate.now(),
                "Test Publisher", 10L, author1, genre1);
        book1.setQuantity(100);
        bookRepository.saveAndFlush(book1);
        Book book2 = new Book("Test Book2", "2234567890", LocalDate.now(),
                "Test Publisher2", 7L, author1, genre1);
        book2.setQuantity(100);
        bookRepository.saveAndFlush(book2);
        Book book3 = new Book("Test Book3", "3234567890", LocalDate.now(),
                "Test Publisher3", 17L, author1, genre2);
        book3.setQuantity(100);
        bookRepository.saveAndFlush(book3);
        Book book4 = new Book("Test Book4", "323456789011", LocalDate.now(),
                "Test Publisher4", 0L, author3, genre3);
        bookRepository.saveAndFlush(book4);
        book4.setQuantity(100);
        List<Long> books = new ArrayList<>();
        books.add(book1.getId());
        books.add(book2.getId());

        User user1 = new User("User1", "user1@gmail.com",
                "password1", UserRole.ROLE_USER, books);
        userRepository.saveAndFlush(user1);
        User user2 = new User("User2", "user2@gmail.com",
                "password2", UserRole.ROLE_USER);
        userRepository.saveAndFlush(user2);
        User user3 = new User("User3", "user3@gmail.com",
                "password3", UserRole.ROLE_USER);
        userRepository.saveAndFlush(user3);
        Set<Long> borrowers = new HashSet<>();
        borrowers.add(user1.getId());
        book1.setBorrowedUserIds(borrowers);
        book2.setBorrowedUserIds(borrowers);
    }
}
