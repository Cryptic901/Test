package com.example.testapp.service;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.model.Book;

import java.util.List;
import java.util.Map;

public interface BookService {

    void setBookParams(Book book, BookDTO bookDTO);

    String deleteBookById(long id);

    BookDTO addBook(BookDTO bookDTO);

    BookDTO getBookById(long id);

    BookDTO getBookByTitle(String title);

    BookDTO getBookByIsbn(String isbn);

    BookDTO updateBookById(long id, BookDTO bookDTO);

    List<BookDTO> getAllBook();

    List<BookShortDTO> getAllBookShort();

    List<BookDTO> getMostPopularBook();

    BookDTO updateBookFields(long id, Map<String, Object> updates);
}
