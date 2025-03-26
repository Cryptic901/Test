package com.example.testapp;

import com.example.testapp.model.Author;
import com.example.testapp.model.Book;
import com.example.testapp.model.Genre;
import com.example.testapp.repository.AuthorRepository;
import com.example.testapp.repository.BookRepository;
import com.example.testapp.repository.GenreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class SetUpForIntegrationTests {

    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected GenreRepository genreRepository;

    @Container
    @ServiceConnection
    public static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    protected final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    void setUpTestData() {
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
        genres2.add(25L);
        genre2.setBooks(genres2);
        genreRepository.saveAndFlush(genre2);

        Genre genre3 = new Genre("genre3", "genre desc3");
        genre3.setCountOfBorrowingBookWithGenre(3L);
        List<Long> genres3 = new ArrayList<>();
        genres3.add(28L);
        genre3.setBooks(genres3);
        genreRepository.saveAndFlush(genre3);

        bookRepository.saveAndFlush(new Book("Test Book", "1234567890", LocalDate.now(),
                "Test Publisher", 10L, author1, genre1));
        bookRepository.saveAndFlush(new Book("Test Book2", "2234567890", LocalDate.now(),
                "Test Publisher2", 7L, author1, genre1));
        bookRepository.saveAndFlush(new Book("Test Book3", "3234567890", LocalDate.now(),
                "Test Publisher3", 17L, author1, genre2));
        bookRepository.saveAndFlush(new Book("Test Book4", "323456789011", LocalDate.now(),
                "Test Publisher4", 0L, author3, genre3));
    }
}
