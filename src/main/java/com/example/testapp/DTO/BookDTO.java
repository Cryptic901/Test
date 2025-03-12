package com.example.testapp.DTO;

import com.example.testapp.model.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

/* Объект для удобной передачи данных о книгах */
public class BookDTO {

    @JsonIgnore
    private long id;
    private String title;
    private String description;
    private String isbn;
    private String publisher;
    private LocalDate publishedDate;
    private int amount;

    private String genreName; //Название жанра
    @JsonProperty(value = "genre_id", access = JsonProperty.Access.WRITE_ONLY)
    private Long genreId;
    private String authorName; //Имя автора
    @JsonProperty(value = "author_id", access = JsonProperty.Access.WRITE_ONLY)
    private Long authorId;

    public static BookDTO fromEntity(Book book) {
        if (book == null) return null;
        BookDTO dto = new BookDTO();
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setIsbn(book.getIsbn());
        dto.setPublisher(book.getPublisher());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setAmount(book.getAmount());
        dto.setAuthorName(book.getAuthor() != null ? book.getAuthor().getName() : "Автор не указан");
        dto.setGenreName(book.getGenre() != null ? book.getGenre().getName() : "Жанр не указан");

        dto.setGenreId(book.getGenre() != null ? book.getGenre().getId() : null);
        dto.setAuthorId(book.getAuthor() != null ? book.getAuthor().getId() : null);

        return dto;
    }

    public BookDTO() {
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

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BookDTO(Long id, String title, String description, String isbn, String publisher, LocalDate publishedDate,
                   int amount, String genreName, Long genreId, String authorName, Long authorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.amount = amount;
        this.genreName = genreName;
        this.genreId = genreId;
        this.authorName = authorName;
        this.authorId = authorId;
    }


    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", amount=" + amount +
                ", genreName='" + genreName + '\'' +
                ", genreId=" + genreId +
                ", authorName='" + authorName + '\'' +
                ", authorId=" + authorId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return amount == bookDTO.amount
                && Objects.equals(id, bookDTO.id)
                && Objects.equals(title, bookDTO.title)
                && Objects.equals(description, bookDTO.description)
                && Objects.equals(isbn, bookDTO.isbn)
                && Objects.equals(publisher, bookDTO.publisher)
                && Objects.equals(publishedDate, bookDTO.publishedDate)
                && Objects.equals(genreName, bookDTO.genreName)
                && Objects.equals(genreId, bookDTO.genreId)
                && Objects.equals(authorName, bookDTO.authorName)
                && Objects.equals(authorId, bookDTO.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, isbn, publisher, publishedDate,
                amount, genreName, genreId, authorName, authorId);
    }
}
