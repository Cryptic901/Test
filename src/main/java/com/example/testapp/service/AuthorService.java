package com.example.testapp.service;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.exceptions.EntityNotFoundException;
import com.example.testapp.model.Authors;
import com.example.testapp.model.Books;
import com.example.testapp.repository.AuthorsRepository;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.GenresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void setAuthorsParams(Authors author, AuthorDTO authorDTO) {
        author.setId(authorDTO.getId());
        author.setName(authorDTO.getName());
        author.setBookList(authorDTO.getBookList());
    }

    public AuthorDTO addAuthor(AuthorDTO authorDTO) {
        Authors author = new Authors();
        setAuthorsParams(author, authorDTO);
        return AuthorDTO.fromEntity(authorsRepository.save(author));
    }

    public AuthorDTO getAuthorById(long id) {
       return AuthorDTO.fromEntity(authorsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id)));
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorsRepository.findAll()
                .stream()
                .map(AuthorDTO::fromEntity)
                .toList();
    }

    public AuthorDTO updateAuthorById(long id, AuthorDTO authorDTO) {
       Authors author = authorsRepository.findById(id)
               .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        setAuthorsParams(author, authorDTO);
       return AuthorDTO.fromEntity(authorsRepository.save(author));
    }

    public void deleteAuthorById(long authorId) {
        if (!authorsRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author not found with id: " + authorId);
        }
        authorsRepository.deleteById(authorId);
    }

    public List<Long> getAllAuthorsBooks(long authorId) {
        if(!authorsRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author not found with id: " + authorId);
        }
        return booksRepository.findByAuthorId(authorId)
                .stream()
                .map(Books::getId)
                .toList();
    }
}
