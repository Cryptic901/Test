package com.example.testapp.DTO;

import com.example.testapp.model.Genres;

import java.util.List;

/* Объект для удобной передачи данных о жанрах */
public class GenreDTO {

    private Long id;

    private String name;

    private List<Long> books;

    private String description;

    private Integer bookCount;


    public static GenreDTO fromEntity(Genres genre) {
        if(genre == null) return null;
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        dto.setDescription(genre.getDescription());
        dto.setBookCount(genre.getBookCount());
        dto.setBooks(genre.getBooks());

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

    public List<Long> getBooks() {
        return books;
    }

    public void setBooks(List<Long> books) {
        this.books = books;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBookCount() {
        return bookCount;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }

    public GenreDTO() {}
    public GenreDTO(String name) {
        this.name = name;
    }

    public GenreDTO(Long id, String name, List<Long> books, String description, Integer bookCount) {
        this.id = id;
        this.name = name;
        this.books = books;
        this.description = description;
        this.bookCount = bookCount;
    }

    @Override
    public String toString() {
        return "GenreDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                ", description='" + description + '\'' +
                ", bookCount=" + bookCount +
                '}';
    }
}
