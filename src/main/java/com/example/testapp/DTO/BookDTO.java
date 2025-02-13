package com.example.testapp.DTO;

import com.example.testapp.model.Books;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/* Объект для удобной передачи данных о книгах */
public class BookDTO implements Serializable {

    private Long id;
    private String title;
    private String status;
    private String description;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private int amount;

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
        dto.setStatus(book.getStatus() != null ? book.getStatus().name() : "UNKNOWN");
        dto.setDescription(book.getDescription());
        dto.setIsbn(book.getIsbn());
        dto.setPublisher(book.getPublisher());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setAmount(book.getAmount());
        dto.setAuthor(book.getAuthor() != null ? book.getAuthor().getName() : "Автор не указан");
        dto.setGenre(book.getGenre() != null ? book.getGenre().getName() : "Жанр не указан");
        dto.setUser(book.getUser() != null ? book.getUser().getUsername() : "Книга свободна");

        dto.setGenreId(book.getGenre() != null ? book.getGenre().getId() : null);
        dto.setUserId(book.getUser() != null ? book.getUser().getId() : null);
        dto.setAuthorId(book.getAuthor() != null ? book.getAuthor().getId() : null);

        return dto;
    }

    public BookDTO() {
    }

    public BookDTO(Long id, String title, String status, String description, String isbn,
                   String publisher, String publishedDate, int amount, String genre, Long genreId, String author, Long authorId,
                   String user, Long userId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.description = description;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.amount = amount;
        this.genre = genre;
        this.genreId = genreId;
        this.author = author;
        this.authorId = authorId;
        this.user = user;
        this.userId = userId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", amount=" + amount +
                ", genre='" + genre + '\'' +
                ", genreId=" + genreId +
                ", author='" + author + '\'' +
                ", authorId=" + authorId +
                ", user='" + user + '\'' +
                ", userId=" + userId +
                '}';
    }
}
