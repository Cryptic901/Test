package com.example.testapp.repository;

import com.example.testapp.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о жанрах */

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findById(long id);

    Optional<Genre> getGenreByName(String name);

    @Query("SELECT g FROM Genre g ORDER BY g.countOfBorrowingBookWithGenre DESC")
    List<Genre> sortByGenrePopularityDescending();

    Optional<Genre> findByName(String name);
}
