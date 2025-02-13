package com.example.testapp.DTO;

import com.example.testapp.model.Genres;

/* Объект для удобной передачи данных о жанрах */
public class GenreDTO {

    private Long id;

    private String name;

    public static GenreDTO fromEntity(Genres genre) {
        if(genre == null) return null;
        GenreDTO dto = new GenreDTO();
        dto.setId(genre.getId());
        dto.setName(genre.getName());

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

    public GenreDTO() {}

    public GenreDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "GenreDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
