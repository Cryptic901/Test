package com.example.testapp.service;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.model.Author;

import java.util.List;
import java.util.Map;

public interface AuthorService {

    void setAuthorParams(Author author, AuthorDTO authorDTO);

    AuthorDTO addAuthor(AuthorDTO authorDTO);

    AuthorDTO getAuthorById(long id);

    AuthorDTO getAuthorByName(String name);

    List<AuthorDTO> getAllAuthor();

    AuthorDTO updateAuthorById(long id, AuthorDTO authorDTO);

    void deleteAuthorById(long authorId);

    List<BookShortDTO> getAllAuthorBook(long authorId);

    AuthorDTO updateAuthorFields(long id, Map<String, Object> updates);
}
