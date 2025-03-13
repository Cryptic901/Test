package com.example.testapp.DTO;

import com.example.testapp.model.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Objects;


/* Объект для удобной передачи данных о книгах в краткой форме */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookShortDTO implements Serializable {

    @JsonIgnore
    private Long id;

    private String title;

    private String description;

    public BookShortDTO(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public BookShortDTO() {}

    public static BookShortDTO fromEntity(Book book) {
        if (book == null) return null;
        BookShortDTO dto = new BookShortDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "BookShortDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookShortDTO that = (BookShortDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }
}
