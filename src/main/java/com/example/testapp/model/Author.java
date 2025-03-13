package com.example.testapp.model;

import jakarta.persistence.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

/* Сущность автор */

@Entity
public class Author implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    private String name;

    private String biography;

    @ElementCollection
    private List<Long> bookList;

    public Author(long authorId) {
        this.id = authorId;
    }

    public Author(String name) {
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

    public List<Long> getBookList() {
        return bookList;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setBookList(List<Long> bookList) {
        this.bookList = bookList;
    }

    public Author(Long id, String name, List<Long> bookList, String biography) {
        this.id = id;
        this.name = name;
        this.bookList = bookList;
        this.biography = biography;
    }
    public Author() {}

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}
