package com.example.testapp.impl;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.model.Author;
import com.example.testapp.repository.AuthorRepository;
import com.example.testapp.repository.BookRepository;
import com.example.testapp.service.AuthorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    }

    @CacheEvict(cacheNames = "authors", key = "#result.id")
    @Transactional
    public AuthorDTO addAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        setAuthorParams(author, authorDTO);
        return AuthorDTO.fromEntity(authorsRepository.save(author));
    }

    @Cacheable(value = "authors", key = "#id")
    @Transactional
    public AuthorDTO getAuthorById(long id) {
        return AuthorDTO.fromEntity(authorsRepository.findById(id)
                .orElse(null));
    }

    @Cacheable(value = "authors", key = "#name")
    @Transactional
    public AuthorDTO getAuthorByName(String name) {
        return AuthorDTO.fromEntity(authorsRepository.findAuthorByName(name)
                .orElse(null));
    }

    public List<AuthorDTO> getAllAuthor() {
        return authorsRepository.findAll()
                .stream()
                .map(AuthorDTO::fromEntity)
                .toList();
    }

    @CacheEvict(cacheNames = "authors", key = "#id")
    @Transactional
    public AuthorDTO updateAuthorById(long id, AuthorDTO authorDTO) {
        Author author = authorsRepository.findById(id)
                .orElse(null);
        if (author != null) {
            setAuthorParams(author, authorDTO);
            return AuthorDTO.fromEntity(authorsRepository.save(author));
        } else {
            return null;
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "authors", key = "#authorId"),
            @CacheEvict(value = "authors", allEntries = true)
    })
    public String deleteAuthorById(long authorId) {
        boolean exists = authorsRepository.existsById(authorId);
        if (!exists) {
            return "Author not found";
        }
        booksRepository.detachBooksFromAuthor(authorId);
        authorsRepository.deleteById(authorId);
        return "Author deleted successfully";
    }

    public List<BookShortDTO> getAllAuthorBook(long authorId) {
        boolean exists = authorsRepository.existsById(authorId);
        if (!exists) {
            return new ArrayList<>();
        }
        return booksRepository.findByAuthorId(authorId)
                .stream()
                .map(BookShortDTO::fromEntity)
                .toList();
    }
    @CacheEvict(cacheNames = "authors", key = "#id")
    @Transactional
    public AuthorDTO updateAuthorFields(long id, Map<String, Object> updates) {
        Author author = authorsRepository.findById(id)
                .orElse(null);

        if (author != null) {
            if (updates.containsKey("name")) {
                author.setName((String) updates.get("name"));
            }
            if (updates.containsKey("bookList") && updates.get("bookList") instanceof List<?> objList) {
                List<Long> list = objList.stream()
                        .map(obj -> Long.valueOf(obj.toString())).toList();
                author.setBookList(new ArrayList<>(list));
            }
            authorsRepository.save(author);
        }
        return AuthorDTO.fromEntity(author);
    }
}
