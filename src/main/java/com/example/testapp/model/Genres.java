package com.example.testapp.model;

import jakarta.persistence.*;

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

    private Integer bookCount;

    private Long countOfBorrowingBookWithGenre;

    public Genres(long genreId) {
        this.id = genreId;
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
        return books;
    }

    public void setBooks(List<Long> books) {
        this.books = books;
    }

    public Integer getBookCount() {
        if (bookCount == null) {
            bookCount = 0;
        }
        return bookCount;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
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
                ", bookCount=" + bookCount +
                ", countOfBorrowingBookWithGenre=" + countOfBorrowingBookWithGenre +
                '}';
    }

    public Genres(Long id, String name, List<Long> books, String description, Integer bookCount, Long countOfBorrowingBookWithGenre) {
        this.id = id;
        this.name = name;
        this.books = books;
        this.description = description;
        this.bookCount = bookCount;
        this.countOfBorrowingBookWithGenre = countOfBorrowingBookWithGenre;
    }

    public Genres() {
    }
}
