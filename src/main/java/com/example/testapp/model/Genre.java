package com.example.testapp.model;

import jakarta.persistence.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* Сущность жанр */

@Entity
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ElementCollection
    private List<Long> books;

    private String description;

    private Integer countOfBookInThatGenre;

    private Long countOfBorrowingBookWithGenre;

    public Genre(long genreId) {
        this.id = genreId;
    }

    public Genre(String name) {
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

    public List<Long> getBook() {
        if (books == null) {
            books = new ArrayList<>();
        }
        return books;
    }

    public void setBook(List<Long> books) {
        this.books = books;
    }

    public Integer getCountOfBookInThatGenre() {
        if (countOfBookInThatGenre == null && books == null) {
            countOfBookInThatGenre = 0;
            books = new ArrayList<>();
        }
        return books.size();
    }

    public void setCountOfBookInThatGenre(Integer countOfBookInThatGenre) {
        this.countOfBookInThatGenre = countOfBookInThatGenre;
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
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                ", description='" + description + '\'' +
                ", countOfBookInThatGenre=" + countOfBookInThatGenre +
                ", countOfBorrowingBookWithGenre=" + countOfBorrowingBookWithGenre +
                '}';
    }

    public Genre(Long id, String name, List<Long> books, String description, Integer countOfBookInThatGenre, Long countOfBorrowingBookWithGenre) {
        this.id = id;
        this.name = name;
        this.books = books;
        this.description = description;
        this.countOfBookInThatGenre = countOfBookInThatGenre;
        this.countOfBorrowingBookWithGenre = countOfBorrowingBookWithGenre;
    }

    public Genre() {
    }
}
