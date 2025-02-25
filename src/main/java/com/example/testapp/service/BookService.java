package com.example.testapp.service;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.model.Books;

import java.util.List;
import java.util.Map;

public interface BookService {

    void setBookParams(Books book, BookDTO bookDTO);

    void deleteBookById(long id);

    BookDTO addBook(BookDTO bookDTO);

    BookDTO getBookById(long id);

    BookDTO getBookByTitle(String title);

    BookDTO getBookByIsbn(String isbn);

    BookDTO updateBookById(long id, BookDTO bookDTO);

    List<BookDTO> getAllBooks();

    List<BookShortDTO> getAllBooksShort();

    List<BookDTO> getMostPopularBooks();

    BookDTO updateBookFields(long id, Map<String, Object> updates);
}
