package com.example.testapp.service.impl;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.exceptions.EntityNotFoundException;
import com.example.testapp.model.Authors;
import com.example.testapp.model.Books;
import com.example.testapp.model.Genres;
import com.example.testapp.repository.AuthorsRepository;
import com.example.testapp.repository.BooksRepository;
import com.example.testapp.repository.GenresRepository;
import com.example.testapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/* Сервис для обработки данных о книгах */

@Service
public class BookServiceImpl implements BookService {

    private final BooksRepository booksRepository;
    private final AuthorsRepository authorsRepository;
    private final GenresRepository genresRepository;

    @Autowired
    public BookServiceImpl(BooksRepository booksRepository, GenresRepository genresRepository, AuthorsRepository authorsRepository) {
        this.booksRepository = booksRepository;
        this.genresRepository = genresRepository;
        this.authorsRepository = authorsRepository;
    }

    //Метод для получения значений из BookDTO и установления их в book
    public void setBookParams(Books book, BookDTO bookDTO) {
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublishedDate(bookDTO.getPublishedDate());
        book.setIsbn(bookDTO.getIsbn());
        book.setAmount(bookDTO.getAmount());
        if (bookDTO.getAuthorId() != null) {
            book.setAuthor(authorsRepository.findById(bookDTO.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + bookDTO.getAuthorId())));
        }
        if (bookDTO.getGenreId() != null) {
            book.setGenre(genresRepository.findById(bookDTO.getGenreId())
                    .orElseThrow(() -> new EntityNotFoundException("Genre not found with id " + bookDTO.getGenreId())));
        }
    }

    //Метод для удаления книги
    @Transactional
    public void deleteBookById(long id) {
        if (!booksRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id " + id);
        }
        Books book = booksRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        Authors authors = authorsRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + book.getAuthor().getId()));
        authors.getBookList().remove(book.getId());
        authorsRepository.save(authors);
        booksRepository.deleteById(id);
    }

    //Метод для добавления книги
    public BookDTO addBook(BookDTO bookDTO) {
        Books bookToAdd = new Books();
        setBookParams(bookToAdd, bookDTO);
        Books savedBook = booksRepository.save(bookToAdd);

        Authors author = authorsRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + bookDTO.getAuthorId()));

        Genres genre = genresRepository.findById(bookDTO.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id " + bookDTO.getGenreId()));

        List<Long> authorBookList = author.getBookList();
        if (authorBookList == null) {
            authorBookList = new ArrayList<>();
        }

        authorBookList.add(savedBook.getId());
        author.setBookList(authorBookList);

        List<Long> booksWithThatGenre = genre.getBooks();
        booksWithThatGenre.add(savedBook.getId());
        genre.setBooks(booksWithThatGenre);

        authorsRepository.save(author);
        genresRepository.save(genre);
        return BookDTO.fromEntity(savedBook);
    }

    //Метод для получения книги по ID
    public BookDTO getBookById(long id) {
        return BookDTO.fromEntity(booksRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id)));
    }

    public BookDTO getBookByTitle(String title) {
        return BookDTO.fromEntity(booksRepository.findBooksByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with title: " + title)));
    }

    public BookDTO getBookByIsbn(String isbn) {
        return BookDTO.fromEntity(booksRepository.findByIsbn(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with isbn: " + isbn)));
    }

    //Метод для обновления книги по ID
    public BookDTO updateBookById(long id, BookDTO bookDTO) {
        Books existingBook = booksRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
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

    //Метод для получения списка книг с более маленьким количеством параметров
    public List<BookShortDTO> getAllBooksShort() {
        List<Books> books = booksRepository.findAll();
        return books.stream()
                .map(BookShortDTO::fromEntity)
                .toList();
    }

    public List<BookDTO> getMostPopularBooks() {
        List<Books> books = booksRepository.sortByBookPopularityDescending();
        return books.stream().map(BookDTO::fromEntity).toList();
    }

    public BookDTO updateBookFields(long id, Map<String, Object> updates) {
        Books books = booksRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        if (updates.containsKey("title")) {
            books.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("description")) {
            books.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("publisher")) {
            books.setPublisher((String) updates.get("publisher"));
        }
        if (updates.containsKey("publishedDate")) {
            books.setPublishedDate((String) updates.get("publishedDate"));
        }
        if (updates.containsKey("isbn")) {
            books.setIsbn((String) updates.get("isbn"));
        }
        if (updates.containsKey("amount")) {
            books.setAmount((Integer) updates.get("amount"));
        }
        if (updates.containsKey("borrowedUserIds") && updates.get("borrowedUserIds") instanceof List<?> userids) {
            List<Long> borrowedUserIds = userids.stream()
                    .map(obj -> Long.valueOf(obj.toString())).toList();
            books.setBorrowedUserIds(new HashSet<>(borrowedUserIds));

        }

        if (updates.containsKey("author_id")) {
            Long authorId = Long.parseLong(updates.get("author_id").toString());
            Authors authors = authorsRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + authorId));
            books.setAuthor(authors);
        }
        if (updates.containsKey("genre_id")) {
            Long genreId = Long.parseLong(updates.get("genre_id").toString());
            Genres genres = genresRepository.findById(genreId)
                    .orElseThrow(() -> new EntityNotFoundException("Genre not found with id " + genreId));
            books.setGenre(genres);
            genresRepository.save(genres);
        }

        if (updates.containsKey("authorName")) {
            String name = updates.get("authorName").toString();
            Authors authors = authorsRepository.findAuthorsByName(name)
                            .orElseThrow(() -> new EntityNotFoundException("Author not found with name " + name));
            books.setAuthor(authors);
        }

        if (updates.containsKey("genreName")) {
            String name = (String) updates.get("genreName");
            Genres genres = genresRepository.findByName(name)
                            .orElseThrow(() -> new EntityNotFoundException("Genre not found with name " + name));
            books.setGenre(genres);
            genres.setCountOfBooksInThatGenre(genres.getCountOfBooksInThatGenre() + 1);
            genresRepository.save(genres);
        }
        booksRepository.save(books);
        return BookDTO.fromEntity(books);
    }
}
