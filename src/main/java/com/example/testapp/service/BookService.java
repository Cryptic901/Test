package com.example.testapp.service;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.model.Books;
import com.example.testapp.model.Users;
import com.example.testapp.repository.AuthorsRepository;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.GenresRepository;
import com.example.testapp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/// Сервис для реализации логики работы контроллеров

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

    public void deleteBookById(long id) {
        booksRepository.deleteById(id);
    }


    public void setBookParams(Books book, BookDTO bookDTO) {
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setStatus(Books.BookStatus.valueOf(bookDTO.getStatus()));

        if(bookDTO.getAuthorId() != null) {
            book.setAuthor(authorsRepository.findById(bookDTO.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Автор с id " + bookDTO.getAuthorId() + "не найден")));
        }

        if (bookDTO.getGenreId() != null) {
            book.setGenre(genresRepository.findById(bookDTO.getGenreId())
                    .orElseThrow(() -> new RuntimeException("Жанр с id " + bookDTO.getGenreId() + "не найден")));
        }

        if (bookDTO.getUserId() != null) {
            book.setUser(usersRepository.findById(bookDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Пользователь с id " + bookDTO.getUserId() + "не найден")));
        }
    }

    public BookDTO addBook(BookDTO bookDTO) {
        Books bookToAdd = new Books();
        setBookParams(bookToAdd, bookDTO);
        return BookDTO.fromEntity(booksRepository.save(bookToAdd));
    }

    public BookDTO getBookById(long id) {
        return BookDTO.fromEntity(booksRepository.findById(id));
    }

    public BookDTO updateBookById(long id, BookDTO bookDTO) {
        Books existingBook = booksRepository.findById(id);
        setBookParams(existingBook, bookDTO);
        return BookDTO.fromEntity(booksRepository.save(existingBook));
    }

    public List<BookDTO> getAllBooks() {
        List<Books> books = booksRepository.findAll();
        return books.stream()
                .map(BookDTO::fromEntity)
                .toList();
    }

    public String borrowBookById(long bookId, long userId) {
        Users user = usersRepository.findById(userId);
        Books book = booksRepository.findById(bookId);

        if (book.getUser() != null) {
            throw new RuntimeException("Book is already borrowed!");
        }

        book.setUser(user);
        booksRepository.save(book);

        return "Book borrowed successfully!";
    }

    public String returnBookById(long userId, long bookId) {

        Books book = booksRepository.findById(bookId);

        if (book.getUser() == null || !book.getUser().getId().equals(userId)) {
            throw new RuntimeException("Book is not borrowed!");
        }

        book.setUser(null);
        booksRepository.save(book);

        return "Book returned successfully!";
    }
}
