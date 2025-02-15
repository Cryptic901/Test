package com.example.testapp.DTO;

import com.example.testapp.model.Books;

import java.util.Objects;


/* Объект для удобной передачи данных о книгах в краткой форме */
public class BookShortDTO {

    private Long id;

    private String title;

    public BookShortDTO(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public BookShortDTO() {}

    public static BookShortDTO fromEntity(Books book) {
        if (book == null) return null;
        BookShortDTO dto = new BookShortDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());

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

    @Override
    public String toString() {
        return "BookShortDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookShortDTO that = (BookShortDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
