package com.example.testapp.service;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.model.Authors;

import java.util.List;
import java.util.Map;

public interface AuthorService {

    void setAuthorsParams(Authors author, AuthorDTO authorDTO);

    AuthorDTO addAuthor(AuthorDTO authorDTO);

    AuthorDTO getAuthorById(long id);

    AuthorDTO getAuthorByName(String name);

    List<AuthorDTO> getAllAuthors();

    AuthorDTO updateAuthorById(long id, AuthorDTO authorDTO);

    void deleteAuthorById(long authorId);

    List<BookShortDTO> getAllAuthorsBooks(long authorId);

    AuthorDTO updateAuthorFields(long id, Map<String, Object> updates);
}
