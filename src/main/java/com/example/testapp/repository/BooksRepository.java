package com.example.testapp.repository;

import com.example.testapp.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о книгах */

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByIsbn(String isbn);

    List<Books> findByAuthorId(long authorId);

    @Modifying
    @Query("UPDATE Books b SET b.genre = NULL WHERE b.genre.id = :genreId")
    void clearGenreByGenreId(@Param("genreId") long id);

    @Query("SELECT b FROM Books b ORDER BY b.countOfBorrowingBook DESC")
    List<Books> sortByBookPopularityDescending();

    Optional<Books> findBooksByTitle(String title);
}
