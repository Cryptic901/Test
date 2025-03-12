package com.example.testapp.impl;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.model.Author;
import com.example.testapp.model.Book;
import com.example.testapp.model.Genre;
import com.example.testapp.repository.AuthorRepository;
import com.example.testapp.repository.BookRepository;
import com.example.testapp.repository.GenreRepository;
import com.example.testapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;


/* Сервис для обработки данных о книгах */

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository booksRepository;
    private final AuthorRepository authorsRepository;
    private final GenreRepository genresRepository;

    @Autowired
    public BookServiceImpl(BookRepository booksRepository, GenreRepository genresRepository, AuthorRepository authorsRepository) {
        this.booksRepository = booksRepository;
        this.genresRepository = genresRepository;
        this.authorsRepository = authorsRepository;
    }

    //Метод для получения значений из BookDTO и установления их в book
    public void setBookParams(Book book, BookDTO bookDTO) {
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublishedDate(LocalDate.now());
        book.setIsbn(bookDTO.getIsbn());
        book.setAmount(bookDTO.getAmount());
        if (bookDTO.getAuthorId() != null) {
            book.setAuthor(authorsRepository.findById(bookDTO.getAuthorId())
                    .orElse(null));
        }
        if (bookDTO.getGenreId() != null) {
            book.setGenre(genresRepository.findById(bookDTO.getGenreId())
                    .orElse(null));
        }
    }

    //Метод для удаления книги
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "books", key = "#id"),
            @CacheEvict(value = "books", allEntries = true)
    })
    public String deleteBookById(long id) {
        boolean exists = booksRepository.existsById(id);
        Book book = booksRepository.findById(id)
                .orElse(null);
        if (book == null || !exists) {
            return "Book not found";
        }
        Author authors = authorsRepository.findById(book.getAuthor().getId())
                .orElse(null);
        if (authors == null) {
            return "Books author not found";
        }
        authors.getBookList().remove(book.getId());
        authorsRepository.save(authors);
        booksRepository.deleteById(id);
        return "Book deleted successfully";
    }

    //Метод для добавления книги
    @CacheEvict(cacheNames = "books", key = "#bookDTO.id")
    @Transactional
    public BookDTO addBook(BookDTO bookDTO) {
        Book bookToAdd = new Book();
        setBookParams(bookToAdd, bookDTO);
        Book savedBook = booksRepository.save(bookToAdd);

        Author author = authorsRepository.findById(bookDTO.getAuthorId())
                .orElse(null);

        Genre genre = genresRepository.findById(bookDTO.getGenreId())
                .orElse(null);
        if (author != null && genre != null) {

            List<Long> authorBookList = author.getBookList();
            if (authorBookList == null) {
                authorBookList = new ArrayList<>();
            }

            authorBookList.add(savedBook.getId());
            author.setBookList(authorBookList);

            List<Long> booksWithThatGenre = genre.getBook();
            booksWithThatGenre.add(savedBook.getId());
            genre.setBook(booksWithThatGenre);

            authorsRepository.save(author);
            genresRepository.save(genre);
        }
        return BookDTO.fromEntity(savedBook);
    }

    //Метод для получения книги по ID
    @Cacheable(value = "books", key = "#id")
    @Transactional
    public BookDTO getBookById(long id) {
        return BookDTO.fromEntity(booksRepository.findById(id)
                .orElse(null));
    }
    @Cacheable(value = "books", key = "#title")
    @Transactional
    public BookDTO getBookByTitle(String title) {
        return BookDTO.fromEntity(booksRepository.findBookByTitle(title)
                .orElse(null));
    }
    @Cacheable(value = "books", key = "#isbn")
    @Transactional
    public BookDTO getBookByIsbn(String isbn) {
        return BookDTO.fromEntity(booksRepository.findByIsbn(isbn)
                .orElse(null));
    }

    //Метод для обновления книги по ID
    @CacheEvict(cacheNames = "books", key = "#id")
    @Transactional
    public BookDTO updateBookById(long id, BookDTO bookDTO) {
        Book existingBook = booksRepository.findById(id)
                .orElse(null);
        if (existingBook != null) {
            setBookParams(existingBook, bookDTO);
            return BookDTO.fromEntity(booksRepository.save(existingBook));
        } else {
            return null;
        }
    }

    //Метод для получения всего списка книг
    public List<BookDTO> getAllBook() {
        List<Book> books = booksRepository.findAll();
        return books.stream()
                .map(BookDTO::fromEntity)
                .toList();
    }

    //Метод для получения списка книг с более маленьким количеством параметров
    public List<BookShortDTO> getAllBookShort() {
        List<Book> books = booksRepository.findAll();
        return books.stream()
                .map(BookShortDTO::fromEntity)
                .toList();
    }

    public List<BookDTO> getMostPopularBook() {
        List<Book> books = booksRepository.sortByBookPopularityDescending();
        return books.stream().map(BookDTO::fromEntity).toList();
    }
    @CacheEvict(cacheNames = "books", key = "#id")
    @Transactional
    public BookDTO updateBookFields(long id, Map<String, Object> updates) {
        Book books = booksRepository.findById(id)
                .orElse(null);
        if (books != null) {
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
                books.setPublishedDate((LocalDate) updates.get("publishedDate"));
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
                Author authors = authorsRepository.findById(authorId)
                        .orElse(null);
                books.setAuthor(authors);
            }
            if (updates.containsKey("genre_id")) {
                Long genreId = Long.parseLong(updates.get("genre_id").toString());
                Genre genres = genresRepository.findById(genreId)
                        .orElse(null);
                books.setGenre(genres);
                genresRepository.save(Objects.requireNonNull(genres));
            }

            if (updates.containsKey("authorName")) {
                String name = updates.get("authorName").toString();
                Author authors = authorsRepository.findAuthorByName(name)
                        .orElse(null);
                books.setAuthor(authors);
            }

            if (updates.containsKey("genreName")) {
                String name = (String) updates.get("genreName");
                Genre genres = genresRepository.findByName(name)
                        .orElse(null);
                books.setGenre(genres);
                Objects.requireNonNull(genres).setCountOfBookInThatGenre(genres.getCountOfBookInThatGenre() + 1);
                genresRepository.save(genres);
            }
            booksRepository.save(books);
            return BookDTO.fromEntity(books);
        } else {
            return null;
        }
    }
}
