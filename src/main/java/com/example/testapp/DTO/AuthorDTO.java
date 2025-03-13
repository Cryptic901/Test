package com.example.testapp.DTO;

/* Объект для удобной передачи данных об авторах */

import com.example.testapp.model.Author;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorDTO implements Serializable {

    @JsonIgnore
    private Long id;

    private String name;

    private String biography;

    public static AuthorDTO fromEntity(Author authors) {
        if(authors == null) return null;
        AuthorDTO dto = new AuthorDTO();
        dto.setId(authors.getId());
        dto.setName(authors.getName());
        dto.setBiography(authors.getBiography());

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

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public AuthorDTO() {}
    public AuthorDTO(long id) {
        this.id = id;
    }

    public AuthorDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "AuthorDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}
