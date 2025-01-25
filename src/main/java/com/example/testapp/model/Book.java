package com.example.testapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique=true, name = "book_id")
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "author_id")
    @ToString.Exclude
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")
    @ToString.Exclude
    private Genre genre;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Column(length = 1000)
    private String description;

    public enum BookStatus {
        AVAILABLE,
        BORROWED,
        RETURNED,
    }
}
