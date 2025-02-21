package com.example.testapp.DTO;

import com.example.testapp.model.Authors;
import com.example.testapp.model.Books;
import com.example.testapp.model.Genres;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/* Объект для удобной передачи данных о книгах */
public class BookDTO implements Serializable {

    private Long id;
    private String title;
    private String description;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private int amount;
    private Set<Long> borrowedUserIds;
    private Long countOfBorrowingBook;

    private String genreName; //Название жанра
    @JsonProperty(value = "genre_id", access = JsonProperty.Access.WRITE_ONLY)
    private Long genreId;
    private String authorName; //Имя автора
    @JsonProperty(value = "author_id", access = JsonProperty.Access.WRITE_ONLY)
    private Long authorId;
    @JsonProperty(value = "user_id", access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    public static BookDTO fromEntity(Books book) {
        if (book == null) return null;
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setIsbn(book.getIsbn());
        dto.setPublisher(book.getPublisher());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setAmount(book.getAmount());
        dto.setCountOfBorrowingBook(book.getCountOfBorrowingBook());
        dto.setAuthorName(book.getAuthor() != null ? book.getAuthor().getName() : "Автор не указан");
        dto.setGenreName(book.getGenre() != null ? book.getGenre().getName() : "Жанр не указан");
        dto.setBorrowedUserIds(book.getBorrowedUserIds() != null ? book.getBorrowedUserIds() : Collections.emptySet());

        dto.setGenreId(book.getGenre() != null ? book.getGenre().getId() : null);
        dto.setAuthorId(book.getAuthor() != null ? book.getAuthor().getId() : null);

        return dto;
    }

    public static Books toEntity(BookDTO dto) {
        if (dto == null) return null;
        Books book = new Books();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        book.setIsbn(dto.getIsbn());
        book.setPublisher(dto.getPublisher());
        book.setPublishedDate(dto.getPublishedDate());
        book.setAmount(dto.getAmount());
        book.setCountOfBorrowingBook(dto.getCountOfBorrowingBook());
        if (dto.getAuthorId() != null) {
            Authors authors = new Authors();
            authors.setId(dto.getAuthorId());
            book.setAuthor(authors);
        }
        if (dto.getGenreId() != null) {
            Genres genres = new Genres();
            genres.setId(dto.getGenreId());
            book.setGenre(genres);
        }
        if (dto.getBorrowedUserIds() != null && !dto.getBorrowedUserIds().isEmpty()) {
            book.setBorrowedUserIds(dto.getBorrowedUserIds());
        } else {
            book.setBorrowedUserIds(Collections.emptySet());
        }
        return book;
    }

    public BookDTO() {
    }

    public BookDTO(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getCountOfBorrowingBook() {
        return countOfBorrowingBook;
    }

    public void setCountOfBorrowingBook(Long countOfBorrowingBook) {
        this.countOfBorrowingBook = countOfBorrowingBook;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Set<Long> getBorrowedUserIds() {
        return borrowedUserIds;
    }

    public void setBorrowedUserIds(Set<Long> borrowedUserIds) {
        this.borrowedUserIds = borrowedUserIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }



    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BookDTO(Long id, String title, String description, String isbn, String publisher, String publishedDate,
                   int amount, Set<Long> borrowedUserIds, String genreName, Long genreId, String authorName, Long authorId, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.amount = amount;
        this.borrowedUserIds = borrowedUserIds;
        this.genreName = genreName;
        this.genreId = genreId;
        this.authorName = authorName;
        this.authorId = authorId;
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return amount == bookDTO.amount && Objects.equals(id, bookDTO.id) && Objects.equals(title, bookDTO.title) && Objects.equals(description, bookDTO.description) && Objects.equals(isbn, bookDTO.isbn) && Objects.equals(publisher, bookDTO.publisher) && Objects.equals(publishedDate, bookDTO.publishedDate) && Objects.equals(borrowedUserIds, bookDTO.borrowedUserIds) && Objects.equals(genreName, bookDTO.genreName) && Objects.equals(genreId, bookDTO.genreId) && Objects.equals(authorName, bookDTO.authorName) && Objects.equals(authorId, bookDTO.authorId) && Objects.equals(userId, bookDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, isbn, publisher, publishedDate, amount, borrowedUserIds, genreName, genreId, authorName, authorId, userId);
    }
}
