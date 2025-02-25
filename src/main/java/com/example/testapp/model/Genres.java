package com.example.testapp.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/* Сущность жанр */

@Entity
public class Genres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ElementCollection
    private List<Long> books;

    private String description;

    private Integer countOfBooksInThatGenre;

    private Long countOfBorrowingBookWithGenre;

    public Genres(long genreId) {
        this.id = genreId;
    }

    public Genres(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getBooks() {
        if (books == null) {
            books = new ArrayList<>();
        }
        return books;
    }

    public void setBooks(List<Long> books) {
        this.books = books;
    }

    public Integer getCountOfBooksInThatGenre() {
        if (countOfBooksInThatGenre == null && books == null) {
            countOfBooksInThatGenre = 0;
            books = new ArrayList<>();
        }
        return books.size();
    }

    public void setCountOfBooksInThatGenre(Integer countOfBooksInThatGenre) {
        this.countOfBooksInThatGenre = countOfBooksInThatGenre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCountOfBorrowingBookWithGenre() {
        if(countOfBorrowingBookWithGenre == null) {
            countOfBorrowingBookWithGenre = 0L;
        }
        return countOfBorrowingBookWithGenre;
    }

    public void setCountOfBorrowingBookWithGenre(Long countOfBorrowingBookWithGenre) {
        this.countOfBorrowingBookWithGenre = countOfBorrowingBookWithGenre;
    }

    @Override
    public String toString() {
        return "Genres{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                ", description='" + description + '\'' +
                ", countOfBooksInThatGenre=" + countOfBooksInThatGenre +
                ", countOfBorrowingBookWithGenre=" + countOfBorrowingBookWithGenre +
                '}';
    }

    public Genres(Long id, String name, List<Long> books, String description, Integer countOfBooksInThatGenre, Long countOfBorrowingBookWithGenre) {
        this.id = id;
        this.name = name;
        this.books = books;
        this.description = description;
        this.countOfBooksInThatGenre = countOfBooksInThatGenre;
        this.countOfBorrowingBookWithGenre = countOfBorrowingBookWithGenre;
    }

    public Genres() {
    }
}
