package com.example.testapp.service;

import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.model.Genres;
import com.example.testapp.repository.GenresRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {

    @Mock
    private GenresRepository genresRepository;

    @InjectMocks
    private GenreService genreService;


    @Test
    void addGenre() {
        Genres genres = new Genres();
        genres.setId(1L);

        when(genresRepository.save(any(Genres.class))).thenReturn(genres);
        GenreDTO genre = genreService.addGenre(GenreDTO.fromEntity(genres));

        assertEquals(genres.getId(), genre.getId());
        verify(genresRepository, times(1)).save(any(Genres.class));
    }

    @Test
    void getAllGenres() {
        Genres genres = new Genres();
        genres.setId(1L);
        Genres genres2 = new Genres();
        genres2.setId(2L);
        List<Genres> genresList = List.of(genres, genres2);

        when(genresRepository.findAll()).thenReturn(genresList);

        List<GenreDTO> genresDTOList = genreService.getAllGenres();

        assertEquals(genresList.get(0).getId(), genresDTOList.get(0).getId());
        assertEquals(genresList.get(1).getId(), genresDTOList.get(1).getId());
        verify(genresRepository, times(1)).findAll();
    }

    @Test
    void getGenreById() {
        Genres genres = new Genres();
        genres.setId(1L);

        when(genresRepository.findById(eq(1L))).thenReturn(Optional.of(genres));

        GenreDTO genreDTO = genreService.getGenreById(1L);

        assertEquals(genres.getId(), genreDTO.getId());
        verify(genresRepository, times(1)).findById(eq(1L));
    }

    @Test
    void updateGenreById() {
        Genres genres = new Genres();
        genres.setId(1L);
        //TODO
    }
}
