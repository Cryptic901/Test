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

    @Override
    public String toString() {
        return "Genres{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                ", description'" + description + '\'' +
                ", bookCount='" + bookCount + '\'' +
                '}';
    }

    public Genres(Long id, String name, List<Long> books, String description, Integer bookCount) {
        this.id = id;
        this.name = name;
        this.books = books;
        this.description = description;
        this.bookCount = bookCount;
    }

    public Genres() {
    }
}
