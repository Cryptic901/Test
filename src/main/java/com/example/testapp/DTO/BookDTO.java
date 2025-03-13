package com.example.testapp.DTO;

import com.example.testapp.model.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/* Объект для удобной передачи данных о книгах */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO implements Serializable {

    @JsonIgnore
    private long id;
    private String title;
    private String description;
    private String isbn;
    private String publisher;
    private LocalDate publishedDate;
    private int amount;

    private String genreName;
    private String authorName;

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
                   int amount, String genreName, String authorName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.amount = amount;
        this.genreName = genreName;
        this.authorName = authorName;
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
                ", authorName='" + authorName + '\'' +
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
                && Objects.equals(authorName, bookDTO.authorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, isbn, publisher, publishedDate,
                amount, genreName, authorName);
    }
}
