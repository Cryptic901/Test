package com.example.testapp.model;

import com.example.testapp.enums.BookStatus;
import jakarta.persistence.*;

/* Сущность книга */

@Entity
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "book_id")
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Authors author;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String publishedDate;

    @Column(nullable = false)
    private int amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    private Genres genre;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

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

    public Authors getAuthor() {
        return author;
    }

    public void setAuthor(Authors author) {
        this.author = author;
    }

    public Genres getGenre() {
        return genre;
    }

    public void setGenre(Genres genre) {
        this.genre = genre;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Users getUser() {
            return user;
    }

    public void setUser(Users user) {
        this.user = user;
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

    public Books(Long id, String title, Authors author, String isbn, String publisher, String publishedDate,
                 int amount, Users user, Genres genre, BookStatus status, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.amount = amount;
        this.user = user;
        this.genre = genre;
        this.status = status;
        this.description = description;
    }

    public Books() {
    }

    public Books(long id) {
        this.id = id;
    }

    public Books(long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", amount=" + amount +
                ", user=" + user +
                ", genre=" + genre +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}

