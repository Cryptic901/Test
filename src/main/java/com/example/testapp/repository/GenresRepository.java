package com.example.testapp.repository;

import com.example.testapp.model.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о жанрах */

@Repository
public interface GenresRepository extends JpaRepository<Genres, Long> {

    Optional<Genres> findById(long id);

    Optional<Genres> getGenreByName(String name);

    @Query("SELECT g FROM Genres g ORDER BY g.countOfBorrowingBookWithGenre DESC")
    List<Genres> sortByGenrePopularityDescending();

    Optional<Genres> findByName(String name);
}
