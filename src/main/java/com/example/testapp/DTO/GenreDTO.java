package com.example.testapp.DTO;

import com.example.testapp.model.Genres;

import java.util.ArrayList;
import java.util.List;

/* Объект для удобной передачи данных о жанрах */
public class GenreDTO {

    private Long id;

    private String name;

    private List<Long> books;

    private String description;

    private Integer countOfBooksInThatGenre;

    private Long countOfBorrowingBookWithGenre;

    public GenreDTO(long id) {
        this.id = id;
    }


    public static GenreDTO fromEntity(Genres genre) {
        if (genre == null) return null;
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        dto.setDescription(genre.getDescription());
        dto.setCountOfBooksInThatGenre(genre.getCountOfBooksInThatGenre());
        dto.setBooks(genre.getBooks() != null ? genre.getBooks() : new ArrayList<>());
        dto.setCountOfBorrowingBookWithGenre(genre.getCountOfBorrowingBookWithGenre());

        return dto;
    }

    public static Genres toEntity(GenreDTO genreDTO) {
        if (genreDTO == null) return null;
        Genres genres = new Genres();
        genres.setId(genreDTO.getId());
        genres.setName(genreDTO.getName());
        genres.setDescription(genreDTO.getDescription());
        genres.setCountOfBooksInThatGenre(genreDTO.getCountOfBooksInThatGenre());
        genres.setBooks(genreDTO.getBooks());
        genres.setCountOfBorrowingBookWithGenre(genreDTO.getCountOfBorrowingBookWithGenre());

        return genres;
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

    public Integer getCountOfBooksInThatGenre() {
        return countOfBooksInThatGenre;
    }

    public void setCountOfBooksInThatGenre(Integer countOfBooksInThatGenre) {
        this.countOfBooksInThatGenre = countOfBooksInThatGenre;
    }

    public GenreDTO() {
    }

    public GenreDTO(String name) {
        this.name = name;
    }

    public GenreDTO(Long id, String name, List<Long> books, String description, Integer countOfBooksInThatGenre, Long countOfBorrowingBookWithGenre) {
        this.id = id;
        this.name = name;
        this.books = books;
        this.description = description;
        this.countOfBooksInThatGenre = countOfBooksInThatGenre;
        this.countOfBorrowingBookWithGenre = countOfBorrowingBookWithGenre;
    }

    @Override
    public String toString() {
        return "GenreDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                ", description='" + description + '\'' +
                ", countOfBooksInThatGenre=" + countOfBooksInThatGenre +
                ", countOfBorrowingBookWithGenre=" + countOfBorrowingBookWithGenre +
                '}';
    }
}
