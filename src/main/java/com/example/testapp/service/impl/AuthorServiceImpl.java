package com.example.testapp.service.impl;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.exceptions.EntityNotFoundException;
import com.example.testapp.model.Author;
import com.example.testapp.model.Book;
import com.example.testapp.repository.AuthorRepository;
import com.example.testapp.repository.BookRepository;
import com.example.testapp.service.AuthorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* Сервис для обработки данных об авторах */

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorsRepository;
    private final BookRepository booksRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorsRepository, BookRepository booksRepository) {
        this.authorsRepository = authorsRepository;
        this.booksRepository = booksRepository;
    }

    public void setAuthorParams(Author author, AuthorDTO authorDTO) {
        author.setName(authorDTO.getName());
        author.setBookList(authorDTO.getBookList());
    }

    public AuthorDTO addAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        setAuthorParams(author, authorDTO);
        return AuthorDTO.fromEntity(authorsRepository.save(author));
    }

    public AuthorDTO getAuthorById(long id) {
        return AuthorDTO.fromEntity(authorsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id)));
    }

    public AuthorDTO getAuthorByName(String name) {
        return AuthorDTO.fromEntity(authorsRepository.findAuthorByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with name: " + name)));
    }

    public List<AuthorDTO> getAllAuthor() {
        return authorsRepository.findAll()
                .stream()
                .map(AuthorDTO::fromEntity)
                .toList();
    }

    public AuthorDTO updateAuthorById(long id, AuthorDTO authorDTO) {
        Author author = authorsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        setAuthorParams(author, authorDTO);
        return AuthorDTO.fromEntity(authorsRepository.save(author));
    }

    @Transactional
    public void deleteAuthorById(long authorId) {
        if (!authorsRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author not found with id: " + authorId);
        }

        Author author = authorsRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId));

        if (!author.getBookList().isEmpty()) {
            for (Long id : author.getBookList()) {
                Book book = booksRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

                book.setAuthor(null);
                booksRepository.save(book);
                booksRepository.deleteById(id);
            }
        }
        authorsRepository.deleteById(authorId);
    }

    public List<BookShortDTO> getAllAuthorBook(long authorId) {
        if (!authorsRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author not found with id: " + authorId);
        }
        return booksRepository.findByAuthorId(authorId)
                .stream()
                .map(BookShortDTO::fromEntity)
                .toList();
    }

    public AuthorDTO updateAuthorFields(long id, Map<String, Object> updates) {
        Author author = authorsRepository.findById(id)
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
