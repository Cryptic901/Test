package com.example.testapp.repository;

import com.example.testapp.model.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о жанрах */

@Repository
public interface GenresRepository extends JpaRepository<Genres, Long> {

    Genres findById(long id);
}
