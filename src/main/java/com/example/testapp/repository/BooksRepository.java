package com.example.testapp.repository;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.model.Authors;
import com.example.testapp.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о книгах */

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByIsbn(String isbn);

     List<Books> findByAuthorId(long authorId);
}
