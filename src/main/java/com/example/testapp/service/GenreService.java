package com.example.testapp.service;

import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.model.Genres;

import java.util.List;
import java.util.Map;

public interface GenreService {

    void setGenreParams(GenreDTO genreDTO, Genres genres);

    GenreDTO addGenre(GenreDTO genreDTO);

    List<GenreDTO> getAllGenres();

    GenreDTO getGenreById(long id);

    GenreDTO updateGenreById(long id, GenreDTO genreDTO);

    void deleteGenreById(long id);

    GenreDTO getGenreByName(String name);

    List<GenreDTO> getMostPopularGenres();

    GenreDTO updateGenreFields(long id, Map<String, Object> updates);
}
