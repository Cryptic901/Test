package com.example.testapp.repository;

import com.example.testapp.model.Books;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о книгах */

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

    Books findById(long id);
}
