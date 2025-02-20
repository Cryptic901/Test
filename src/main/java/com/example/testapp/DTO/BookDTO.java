package com.example.testapp.DTO;

import com.example.testapp.model.Books;
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

    private String genre; //Название жанра
    @JsonProperty(value = "genre_id", access = JsonProperty.Access.WRITE_ONLY)
    private Long genreId;
    private String author; //Имя автора
    @JsonProperty(value = "author_id", access = JsonProperty.Access.WRITE_ONLY)
    private Long authorId;
    private String user; //Имя пользователя
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
        dto.setAuthor(book.getAuthor() != null ? book.getAuthor().getName() : "Автор не указан");
        dto.setGenre(book.getGenre() != null ? book.getGenre().getName() : "Жанр не указан");
        dto.setBorrowedUserIds(book.getBorrowedUserIds() != null ? book.getBorrowedUserIds() : Collections.emptySet());

        dto.setGenreId(book.getGenre() != null ? book.getGenre().getId() : null);
        dto.setAuthorId(book.getAuthor() != null ? book.getAuthor().getId() : null);

        return dto;
    }

    public BookDTO() {
    }

    public BookDTO(long id, String title) {
        this.id = id;
        this.title = title;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
                   int amount, Set<Long> borrowedUserIds, String genre, Long genreId, String author, Long authorId, String user, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.amount = amount;
        this.borrowedUserIds = borrowedUserIds;
        this.genre = genre;
        this.genreId = genreId;
        this.author = author;
        this.authorId = authorId;
        this.user = user;
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return amount == bookDTO.amount && Objects.equals(id, bookDTO.id) && Objects.equals(title, bookDTO.title) && Objects.equals(description, bookDTO.description) && Objects.equals(isbn, bookDTO.isbn) && Objects.equals(publisher, bookDTO.publisher) && Objects.equals(publishedDate, bookDTO.publishedDate) && Objects.equals(borrowedUserIds, bookDTO.borrowedUserIds) && Objects.equals(genre, bookDTO.genre) && Objects.equals(genreId, bookDTO.genreId) && Objects.equals(author, bookDTO.author) && Objects.equals(authorId, bookDTO.authorId) && Objects.equals(user, bookDTO.user) && Objects.equals(userId, bookDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, isbn, publisher, publishedDate, amount, borrowedUserIds, genre, genreId, author, authorId, user, userId);
    }
}
