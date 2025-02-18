package com.example.testapp.repository;

import com.example.testapp.model.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о жанрах */

@Repository
public interface GenresRepository extends JpaRepository<Genres, Long> {

    Optional<Genres> findById(long id);

    @Modifying
    @Query("SELECT new com.example.testapp.DTO.GenreDTO(g.name) FROM Genres g WHERE g.name = :genreName")
    Genres getGenreByName(@Param("genreName") String name);
}
