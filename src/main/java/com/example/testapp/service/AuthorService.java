package com.example.testapp.service;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.exceptions.EntityNotFoundException;
import com.example.testapp.model.Authors;
import com.example.testapp.model.Books;
import com.example.testapp.repository.AuthorsRepository;
import com.example.testapp.repository.BooksRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Сервис для обработки данных об авторах */

@Service
public class AuthorService {

    private final AuthorsRepository authorsRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public AuthorService(AuthorsRepository authorsRepository, BooksRepository booksRepository) {
        this.authorsRepository = authorsRepository;
        this.booksRepository = booksRepository;
    }

    public void setAuthorsParams(Authors author, AuthorDTO authorDTO) {
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

    public AuthorDTO getAuthorByName(String name) {
        return AuthorDTO.fromEntity(authorsRepository.findAuthorsByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with name: " + name)));
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

    @Transactional
    public void deleteAuthorById(long authorId) {
        if (!authorsRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author not found with id: " + authorId);
        }

        Authors author = authorsRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId));

        if (!author.getBookList().isEmpty()) {
            for (Long id : author.getBookList()) {
                Books book = booksRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

                book.setAuthor(null);
                booksRepository.save(book);
                booksRepository.deleteById(id);
            }
        }
        authorsRepository.deleteById(authorId);
    }

    public List<BookShortDTO> getAllAuthorsBooks(long authorId) {
        if (!authorsRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author not found with id: " + authorId);
        }
        return booksRepository.findByAuthorId(authorId)
                .stream()
                .map(BookShortDTO::fromEntity)
                .toList();
    }

    public AuthorDTO updateAuthorFields(long id, Map<String, Object> updates) {
        Authors author = authorsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));

        if (updates.containsKey("name")) {
            author.setName((String) updates.get("name"));
        }

        if (updates.containsKey("bookList") && updates.get("bookList") instanceof List<?> objList) {
            List<Long> list = objList.stream()
                    .map(obj -> Long.valueOf(obj.toString())).toList();
            author.setBookList(new ArrayList<>(list));
        }
        authorsRepository.save(author);
        return AuthorDTO.fromEntity(author);
    }
}
