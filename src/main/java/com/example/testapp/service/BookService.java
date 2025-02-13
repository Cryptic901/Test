package com.example.testapp.service;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.enums.BookStatus;
import com.example.testapp.model.Books;
import com.example.testapp.repository.AuthorsRepository;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.GenresRepository;
import com.example.testapp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/* Сервис для обработки данных о книгах */

@Service
public class BookService {

    private final BooksRepository booksRepository;
    private final AuthorsRepository authorsRepository;
    private final UsersRepository usersRepository;
    private final GenresRepository genresRepository;

    @Autowired
    public BookService(BooksRepository booksRepository, UsersRepository usersRepository, GenresRepository genresRepository, AuthorsRepository authorsRepository) {
        this.booksRepository = booksRepository;
        this.usersRepository = usersRepository;
        this.genresRepository = genresRepository;
        this.authorsRepository = authorsRepository;
    }

    //Метод для получения значений из BookDTO и установления их в book
    public void setBookParams(Books book, BookDTO bookDTO) {
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setStatus(BookStatus.valueOf(bookDTO.getStatus()));
        book.setPublisher(bookDTO.getPublisher());
        book.setPublishedDate(bookDTO.getPublishedDate());
        book.setIsbn(bookDTO.getIsbn());
        book.setAmount(bookDTO.getAmount());

        if (bookDTO.getAuthorId() != null) {
            book.setAuthor(authorsRepository.findById(bookDTO.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Author with id " + bookDTO.getAuthorId() + "not found")));
        }

        if (bookDTO.getGenreId() != null) {
            book.setGenre(genresRepository.findById(bookDTO.getGenreId())
                    .orElseThrow(() -> new RuntimeException("Genre with id " + bookDTO.getGenreId() + "not found")));
        }

        if (bookDTO.getUserId() != null) {
            book.setUser(usersRepository.findById(bookDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User with id " + bookDTO.getUserId() + "not found")));
        }
    }

    //Метод для удаления книги
    public void deleteBookById(long id) {
        if (!booksRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id " + id);
        }
        booksRepository.deleteById(id);
    }

    //Метод для добавления книги
    public BookDTO addBook(BookDTO bookDTO) {
        Books bookToAdd = new Books();
        setBookParams(bookToAdd, bookDTO);
        return BookDTO.fromEntity(booksRepository.save(bookToAdd));
    }

    //Метод для получения книги по ID
    public BookDTO getBookById(long id) {
        return BookDTO.fromEntity(booksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id)));
    }

    public BookDTO getBookByIsbn(String isbn) {
        return BookDTO.fromEntity(booksRepository.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with isbn: " + isbn)));
    }

    //Метод для обновления книги по ID
    public BookDTO updateBookById(long id, BookDTO bookDTO) {
        Books existingBook = booksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        setBookParams(existingBook, bookDTO);
        return BookDTO.fromEntity(booksRepository.save(existingBook));
    }

    //Метод для получения всего списка книг
    public List<BookDTO> getAllBooks() {
        List<Books> books = booksRepository.findAll();
        return books.stream()
                .map(BookDTO::fromEntity)
                .toList();
    }
}
