package com.example.testapp.service;

import com.example.testapp.repository.AuthorsRepository;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.GenresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* Сервис для обработки данных об авторах */

@Service
public class AuthorService {

    private final AuthorsRepository authorsRepository;
    private final BooksRepository booksRepository;
    private final GenresRepository genresRepository;

    @Autowired
    public AuthorService(AuthorsRepository authorsRepository, BooksRepository booksRepository, GenresRepository genresRepository) {
        this.authorsRepository = authorsRepository;
        this.booksRepository = booksRepository;
        this.genresRepository = genresRepository;
    }

}
