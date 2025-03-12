package com.example.testapp.DTO;

import com.example.testapp.model.Genre;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/* Объект для удобной передачи данных о жанрах */
public class GenreDTO {
    @JsonIgnore
    private Long id;

    private String name;

    private List<Long> books;

    private String description;

    private Integer countOfBookInThatGenre;

    @JsonIgnore
    private Long countOfBorrowingBookWithGenre;

    public static GenreDTO fromEntity(Genre genre) {
        if (genre == null) return null;
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        dto.setDescription(genre.getDescription() == null ? "Описания нет" : genre.getDescription());
        dto.setCountOfBookInThatGenre(genre.getCountOfBookInThatGenre());
        dto.setBook(genre.getBook() != null ? genre.getBook() : new ArrayList<>());
        dto.setCountOfBorrowingBookWithGenre(genre.getCountOfBorrowingBookWithGenre());

        return dto;
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

    public Long getCountOfBorrowingBookWithGenre() {
        return countOfBorrowingBookWithGenre;
    }

    public void setCountOfBorrowingBookWithGenre(Long countOfBorrowingBookWithGenre) {
        this.countOfBorrowingBookWithGenre = countOfBorrowingBookWithGenre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCountOfBookInThatGenre() {
        return countOfBookInThatGenre;
    }

    public void setCountOfBookInThatGenre(Integer countOfBookInThatGenre) {
        this.countOfBookInThatGenre = countOfBookInThatGenre;
    }

    public GenreDTO() {
    }

    @Override
    public String toString() {
        return "GenreDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                ", description='" + description + '\'' +
                ", countOfBookInThatGenre=" + countOfBookInThatGenre +
                ", countOfBorrowingBookWithGenre=" + countOfBorrowingBookWithGenre +
                '}';
    }
}
