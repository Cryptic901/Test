package com.example.testapp.model;

import jakarta.persistence.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

/* Сущность книга */

@Entity
@Table(name = "book")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private LocalDate publishedDate;

    @Column(nullable = false)
    private int amount;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Long> borrowedUserIds;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private Long countOfBorrowingBook;

    @Column(length = 1000)
    private String description;

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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Set<Long> getBorrowedUserIds() {
        if(borrowedUserIds == null) {
            borrowedUserIds = Collections.emptySet();
        }
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

    public Long getCountOfBorrowingBook() {
        if (countOfBorrowingBook == null) {
            countOfBorrowingBook = 0L;
        }
        return countOfBorrowingBook;
    }

    public void setCountOfBorrowingBook(Long countOfBorrowingBook) {
        this.countOfBorrowingBook = countOfBorrowingBook;
    }

    public Book(Long id, String title, Author author, String isbn, String publisher, LocalDate publishedDate,
                 int amount,Set<Long> borrowedUserIds, Genre genre, String description, Long countOfBorrowingBook) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.amount = amount;
        this.borrowedUserIds = borrowedUserIds;
        this.genre = genre;
        this.description = description;
        this.countOfBorrowingBook = countOfBorrowingBook;
    }

    public Book() {
    }

    public Book(long id) {
        this.id = id;
    }

    public Book(long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", amount=" + amount +
                ", borrowedUserIds=" + borrowedUserIds +
                ", genre=" + genre +
                ", countOfBorrowingBook=" + countOfBorrowingBook +
                ", description='" + description + '\'' +
                '}';
    }
}

