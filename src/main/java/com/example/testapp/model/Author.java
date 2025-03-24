package com.example.testapp.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) &&
                Objects.equals(name, author.name) &&
                Objects.equals(biography, author.biography) &&
                Objects.equals(bookList, author.bookList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, biography, bookList);
    }
}
