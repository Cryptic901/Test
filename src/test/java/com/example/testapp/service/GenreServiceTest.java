package com.example.testapp.service;

import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.model.Genre;
import com.example.testapp.repository.GenreRepository;
import com.example.testapp.impl.GenreServiceImpl;
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
    private GenreRepository genresRepository;

    @InjectMocks
    private GenreServiceImpl genreService;


    @Test
    void addGenre() {
        Genre genres = new Genre();
        genres.setId(1L);

        when(genresRepository.save(any(Genre.class))).thenReturn(genres);
        GenreDTO genre = genreService.addGenre(GenreDTO.fromEntity(genres));

        assertEquals(genres.getId(), genre.getId());
        verify(genresRepository, times(1)).save(any(Genre.class));
    }

    @Test
    void getAllGenre() {
        Genre genres = new Genre();
        genres.setId(1L);
        Genre genres2 = new Genre();
        genres2.setId(2L);
        List<Genre> genresList = List.of(genres, genres2);

        when(genresRepository.findAll()).thenReturn(genresList);

        List<GenreDTO> genresDTOList = genreService.getAllGenre();

        assertEquals(genresList.get(0).getId(), genresDTOList.get(0).getId());
        assertEquals(genresList.get(1).getId(), genresDTOList.get(1).getId());
        verify(genresRepository, times(1)).findAll();
    }

    @Test
    void getGenreById() {
        Genre genres = new Genre();
        genres.setId(1L);

        when(genresRepository.findById(eq(1L))).thenReturn(Optional.of(genres));

        GenreDTO genreDTO = genreService.getGenreById(1L);

        assertEquals(genres.getId(), genreDTO.getId());
        verify(genresRepository, times(1)).findById(eq(1L));
    }

    @Test
    void updateGenreById() {
        long genreId = 1L;
        Genre genres = new Genre();
        genres.setId(genreId);
        genres.setName("Poem");
        Genre genres1 = new Genre();
        genres1.setName("Novel");

        when(genresRepository.findById(genreId)).thenReturn(Optional.of(genres));
        when(genresRepository.save(genres)).thenReturn(genres);

        GenreDTO genreDTO = genreService.updateGenreById(genreId, GenreDTO.fromEntity(genres1));
        assertEquals("Novel", genreDTO.getName());
        verify(genresRepository, times(1)).findById(genreId);
        verify(genresRepository, times(1)).save(genres);
    }

    @Test
    void getGenreByName_Success() {
        String name = "Novel";
        Genre genres = new Genre();
        genres.setName(name);

        when(genresRepository.getGenreByName(name)).thenReturn(Optional.of(genres));
        GenreDTO genreDTO = genreService.getGenreByName(name);

        assertEquals(genreDTO.getName(), genres.getName());
        verify(genresRepository, times(1)).getGenreByName(name);
    }

    @Test
    void getMostPopularGenre_Success() {
        Genre genres = new Genre(1L);
        genres.setCountOfBorrowingBookWithGenre(2L);
        Genre genres1 = new Genre(2L);
        genres1.setCountOfBorrowingBookWithGenre(10L);

        when(genresRepository.sortByGenrePopularityDescending()).thenReturn(List.of(genres1, genres));

        List<GenreDTO> genreDTOs = genreService.getMostPopularGenre();

        assertEquals(List.of(genreDTOs.get(0).getId(), genreDTOs.get(1).getId()), List.of(genres1.getId(), genres.getId()));
        verify(genresRepository, times(1)).sortByGenrePopularityDescending();
    }
}
