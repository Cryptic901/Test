package com.example.testapp.repository;

import com.example.testapp.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных об авторах */

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findById(long id);

    Optional<Author> findAuthorByName(String name);
}
