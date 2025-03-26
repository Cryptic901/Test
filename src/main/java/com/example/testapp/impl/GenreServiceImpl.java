package com.example.testapp.impl;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.model.Genre;
import com.example.testapp.repository.BookRepository;
import com.example.testapp.repository.GenreRepository;
import com.example.testapp.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/* Сервис для обработки данных о жанрах */

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genresRepository;
    private final BookRepository booksRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genresRepository, BookRepository booksRepository) {
        this.genresRepository = genresRepository;
        this.booksRepository = booksRepository;
    }

    public void setGenreParams(GenreDTO genreDTO, Genre genres) {
        genres.setName(genreDTO.getName());
        genres.setDescription(genreDTO.getDescription());
        genres.setCountOfBookInThatGenre(genreDTO.getCountOfBookInThatGenre());
        genres.setCountOfBorrowingBookWithGenre(genreDTO.getCountOfBorrowingBookWithGenre());
    }

    @CacheEvict(value = "genres", key = "#result.id")
    @Transactional
    public GenreDTO addGenre(GenreDTO genreDTO) {
        Genre genres = new Genre();
        setGenreParams(genreDTO, genres);
        return GenreDTO.fromEntity(genresRepository.save(genres));
    }

    public List<GenreDTO> getAllGenre() {
        List<Genre> genres = genresRepository.findAll();
        return genres.stream()
                .map(GenreDTO::fromEntity)
                .toList();
    }
    @Cacheable(value = "genres", key = "#id")
    @Transactional
    public GenreDTO getGenreById(long id) {
        return GenreDTO.fromEntity(genresRepository.findById(id)
                .orElse(null));
    }
    @CacheEvict(value = "genres", key = "#id")
    @Transactional
    public GenreDTO updateGenreById(long id, GenreDTO genreDTO) {
        Genre genres = genresRepository.findById(id)
                .orElse(null);
        if (genres != null) {
            setGenreParams(genreDTO, genres);
            return GenreDTO.fromEntity(genresRepository.save(genres));
        } else {
            return null;
        }
    }

    @Cacheable(value = "genres", key = "#genreId")
    @Transactional
    public List<BookDTO> getAllBooksInGenreById(long genreId) {
        return booksRepository.findAll()
                .stream()
                .filter(book -> book.getGenre().getId() == genreId)
                .map(BookDTO::fromEntity).toList();
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "genres", key = "#id"),
            @CacheEvict(value = "genres", allEntries = true)
    })
    public String deleteGenreById(long id) {
        boolean exists = genresRepository.existsById(id);
        if (!exists) {
            return "Genre not found";
        }
        booksRepository.clearGenreByGenreId(id);
        genresRepository.deleteById(id);
        return "Genre deleted successfully";
    }
    @Cacheable(value = "genres", key = "#name")
    @Transactional
    public GenreDTO getGenreByName(String name) {
        Genre genre = genresRepository.getGenreByName(name)
                .orElse(null);
        return GenreDTO.fromEntity(genre);
    }

    public List<GenreDTO> getMostPopularGenre() {
        List<Genre> genres = genresRepository.sortByGenrePopularityDescending();
        return genres.stream().map(GenreDTO::fromEntity).toList();
    }
    @CacheEvict(value = "genres", key = "#id")
    @Transactional
    public GenreDTO updateGenreFields(long id, Map<String, Object> updates) {
        Genre genres = genresRepository.findById(id)
                .orElse(null);

        if (genres != null) {
            if (updates.containsKey("name")) {
                genres.setName((String) updates.get("name"));
            }
            if (updates.containsKey("description")) {
                genres.setDescription((String) updates.get("description"));
            }

            if (updates.containsKey("books") && updates.get("books") instanceof List<?> objects) {
                List<Long> bookIds = objects.stream()
                        .map(obj -> Long.valueOf(obj.toString())).toList();
                genres.setBooks(new ArrayList<>(bookIds));
            }
            if (updates.containsKey("countOfBookInThatGenre")) {
                genres.setCountOfBookInThatGenre((Integer) updates.get("countOfBookInThatGenre"));
            }

            if (updates.containsKey("countOfBorrowingBookWithGenre")) {
                genres.setCountOfBorrowingBookWithGenre((Long) updates.get("countOfBorrowingBookWithGenre"));
            }
            genresRepository.save(genres);
            return GenreDTO.fromEntity(genres);
        } else {
            return null;
        }
    }
}
