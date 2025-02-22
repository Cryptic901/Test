package com.example.testapp.DTO;

/* Объект для удобной передачи данных об авторах */

import com.example.testapp.model.Authors;

import java.util.ArrayList;
import java.util.List;

public class AuthorDTO {

    private Long id;

    private String name;

    private List<Long> bookList;

    public static AuthorDTO fromEntity(Authors authors) {
        if(authors == null) return null;
        AuthorDTO dto = new AuthorDTO();
        dto.setId(authors.getId());
        dto.setName(authors.getName());
        dto.setBookList(authors.getBookList() != null ? authors.getBookList() : new ArrayList<>());

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

    public List<Long> getBookList() {
        return bookList;
    }

    public void setBookList(List<Long> bookList) {
        this.bookList = bookList;
    }

    public AuthorDTO() {}
    public AuthorDTO(long id) {
        this.id = id;
    }

    public AuthorDTO(Long id, String name, List<Long> bookList) {
        this.id = id;
        this.name = name;
        this.bookList = bookList;
    }

    @Override
    public String toString() {
        return "AuthorDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bookList=" + bookList +
                '}';
    }
}
