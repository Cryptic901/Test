package com.example.testapp.repository;

import com.example.testapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о книгах */

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    List<Book> findByAuthorId(long authorId);

    @Modifying
    @Query("UPDATE Book b SET b.genre = NULL WHERE b.genre.id = :genreId")
    void clearGenreByGenreId(@Param("genreId") long id);

    @Query("SELECT b FROM Book b ORDER BY b.countOfBorrowingBook DESC")
    List<Book> sortByBookPopularityDescending();

    @Modifying
    @Query("UPDATE Book b SET b.author = NULL WHERE b.author.id = :authorId")
    void detachBooksFromAuthor(@Param("authorId") long authorId);

    Optional<Book> findBookByTitle(String title);

    @Query("SELECT b FROM Book b JOIN b.borrowedUserIds u WHERE u = :userId")
    List<Book> getBooksByBorrowedUserIds(@Param("userId") Long userId);
}
