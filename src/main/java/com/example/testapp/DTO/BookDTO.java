package com.example.testapp.DTO;

import com.example.testapp.model.Books;

import java.io.Serializable;

public class BookDTO implements Serializable {

    private Long id;
    private String title;
    private String status;
    private String description;

    private String genre; //Название жанра
    private Long genreId;
    private String author; //Имя автора
    private Long authorId;
    private String user; //Имя пользователя
    private Long userId;


    public static BookDTO fromEntity(Books book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setStatus(book.getStatus() != null ? book.getStatus().name() : "UNKNOWN");
        dto.setDescription(book.getDescription());

        dto.setGenreId(book.getGenre() != null ? book.getGenre().getId() : null);
        dto.setUserId(book.getUser() != null ? book.getUser().getId() : null);
        dto.setAuthorId(book.getAuthor() != null ? book.getAuthor().getId() : null);

        dto.setAuthor(book.getAuthor() != null ? book.getAuthor().getName() : "Автор не указан");
        dto.setGenre(book.getGenre() != null ? book.getGenre().getName() : "Жанр не указан");
        dto.setUser(book.getUser() != null ? book.getUser().getUsername() : "Книга свободна");


        return dto;
    }

    public BookDTO() {
    }

    public BookDTO(Books book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.status = String.valueOf(book.getStatus());
        this.description = book.getDescription();
        this.genre = String.valueOf(book.getGenre());
        this.author = String.valueOf(book.getAuthor());
        this.user = book.getUser().getUsername();
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

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", author='" + author + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
