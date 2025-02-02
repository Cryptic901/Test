package com.example.testapp.service;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.model.Authors;
import com.example.testapp.model.Books;
import com.example.testapp.model.Genres;
import com.example.testapp.model.Users;
import com.example.testapp.repository.AuthorsRepository;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.GenresRepository;
import com.example.testapp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public BookDTO addBook(BookDTO bookDTO) {
        Books bookToAdd = new Books();
        bookToAdd.setTitle(bookDTO.getTitle());
        bookToAdd.setDescription(bookDTO.getDescription());
        bookToAdd.setStatus(Books.BookStatus.valueOf(bookDTO.getStatus()));

        if(bookToAdd.getAuthor() != null) {
            Authors authors = authorsRepository.findById(bookDTO.getAuthorId()).orElseThrow();
            bookToAdd.setAuthor(authors);
        }

        if(bookToAdd.getGenre() != null) {
            Genres genres = genresRepository.findById(bookDTO.getGenreId()).orElseThrow();
            bookToAdd.setGenre(genres);
        }

        if(bookToAdd.getUser() != null) {
            Users users = usersRepository.findById(bookDTO.getUserId()).orElseThrow();
            bookToAdd.setUser(users);
        }

         booksRepository.save(bookToAdd);
         return BookDTO.fromEntity(bookToAdd);
    }

    public Books getBookById(long id) {
        return booksRepository.findById(id);
    }

    public Books updateBookById(long id, Books book) {
        Books existingBook = booksRepository.findById(id);

        existingBook.setId(book.getId());
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setUser(book.getUser());
        existingBook.setStatus(book.getStatus());
        existingBook.setDescription(book.getDescription());
        existingBook.setGenre(book.getGenre());

        return booksRepository.save(existingBook);
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
