package com.example.testapp.service;

import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.exceptions.EntityNotFoundException;
import com.example.testapp.model.Genres;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.GenresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/* Сервис для обработки данных о жанрах */

@Service
public class GenreService {

    private final GenresRepository genresRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public GenreService(GenresRepository genresRepository, BooksRepository booksRepository) {
        this.genresRepository = genresRepository;
        this.booksRepository = booksRepository;
    }

    public void setGenreParams(GenreDTO genreDTO, Genres genres) {
        genres.setId(genreDTO.getId());
        genres.setName(genreDTO.getName());
        genres.setDescription(genreDTO.getDescription());
        genres.setBooks(genreDTO.getBooks());
        genres.setBookCount(genreDTO.getBookCount());
        genres.setCountOfBorrowingBookWithGenre(genreDTO.getCountOfBorrowingBookWithGenre());
    }

    public GenreDTO addGenre(GenreDTO genreDTO) {
        Genres genres = new Genres();
        setGenreParams(genreDTO, genres);
        return GenreDTO.fromEntity(genresRepository.save(genres));
    }

    public List<GenreDTO> getAllGenres() {
        List<Genres> genres = genresRepository.findAll();
        return genres.stream()
                .map(GenreDTO::fromEntity)
                .toList();
    }

    public GenreDTO getGenreById(long id) {
        return GenreDTO.fromEntity(genresRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id)));
    }

    public GenreDTO updateGenreById(long id, GenreDTO genreDTO) {
        Genres genres = genresRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id: " + id));
        setGenreParams(genreDTO, genres);
        return GenreDTO.fromEntity(genresRepository.save(genres));
    }

    @Transactional
    public void deleteGenreById(long id) {
        if (!genresRepository.existsById(id)) {
            throw new EntityNotFoundException("Genre not found with id: " + id);
        }
        booksRepository.clearGenreByGenreId(id);
        genresRepository.deleteById(id);
    }

    public GenreDTO getGenreByName(String name) {
       Genres genre = genresRepository.getGenreByName(name)
               .orElseThrow(() -> new EntityNotFoundException("Genre not found with name: " + name));
       return GenreDTO.fromEntity(genre);
    }

    public List<GenreDTO> getMostPopularGenres() {
        List<Genres> genres = genresRepository.sortByGenrePopularityDescending();
        return genres.stream().map(GenreDTO::fromEntity).toList();
    }
}
