package com.example.testapp.service;

import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.model.Genre;

import java.util.List;
import java.util.Map;

public interface GenreService {

    void setGenreParams(GenreDTO genreDTO, Genre genres);

    GenreDTO addGenre(GenreDTO genreDTO);

    List<GenreDTO> getAllGenre();

    GenreDTO getGenreById(long id);

    GenreDTO updateGenreById(long id, GenreDTO genreDTO);

    String deleteGenreById(long id);

    GenreDTO getGenreByName(String name);

    List<GenreDTO> getMostPopularGenre();

    GenreDTO updateGenreFields(long id, Map<String, Object> updates);
}
