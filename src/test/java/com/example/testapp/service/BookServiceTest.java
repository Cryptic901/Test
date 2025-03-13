package com.example.testapp.service;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.model.Author;
import com.example.testapp.model.Book;
import com.example.testapp.model.Genre;
import com.example.testapp.repository.AuthorRepository;
import com.example.testapp.repository.BookRepository;
import com.example.testapp.repository.GenreRepository;
import com.example.testapp.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository booksRepository;

    @Mock
    private AuthorRepository authorsRepository;

    @Mock
    private GenreRepository genresRepository;

    @InjectMocks
    private BookServiceImpl bookService;


    @Test
    void addBook_Success() {

        long bookId = 1L;
        long authorId = 1L;
        long genreId = 1L;
        Author author = new Author(authorId);
        author.setName("author");
        Genre genre = new Genre(genreId);
        genre.setName("genre");
        Book book = new Book(bookId);
        book.setAuthor(author);
        book.setGenre(genre);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(bookId);
        bookDTO.setTitle("Title");
        bookDTO.setAuthorName(author.getName());
        bookDTO.setGenreName(genre.getName());

        when(booksRepository.save(any(Book.class))).thenReturn(book);
        when(genresRepository.findByName(bookDTO.getGenreName())).thenReturn(Optional.of(genre));
        when(authorsRepository.findAuthorByName(bookDTO.getAuthorName())).thenReturn(Optional.of(author));

        bookService.setBookParams(book, bookDTO);
        BookDTO dto = bookService.addBook(bookDTO);

        assertEquals("Title", dto.getTitle());

        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);
        ArgumentCaptor<Genre> genreCaptor = ArgumentCaptor.forClass(Genre.class);
        verify(authorsRepository, times(1)).save(authorCaptor.capture());
        verify(genresRepository, times(1)).save(genreCaptor.capture());
        Author authors = authorCaptor.getValue();
        Genre genres = genreCaptor.getValue();
        assertTrue(author.getBookList().contains(bookId));
        assertEquals(1, genres.getCountOfBookInThatGenre());
        verify(booksRepository, times(1)).save(any(Book.class));
    }

    @Test
    void deleteBookByIdTest_Success() {
        long bookId = 1L;
        long authorId = 1L;
        Author authors = new Author(authorId);
        authors.setId(authorId);
        authors.setBookList(new ArrayList<>(List.of(bookId)));
        Book books = new Book();
        books.setId(bookId);
        books.setAuthor(authors);

        doNothing().when(booksRepository).deleteById(bookId);
        when(booksRepository.existsById(bookId)).thenReturn(true);
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(books));
        when(authorsRepository.findById(books.getAuthor().getId())).thenReturn(Optional.of(authors));

        bookService.deleteBookById(bookId);

        verify(booksRepository, times(1)).deleteById(bookId);
    }

    @Test
    void deleteBookByIdTest_NotFound() {
        long id = 1L;

        assertEquals("Book not found", bookService.deleteBookById(id));

        verify(booksRepository, never()).deleteById(id);
    }

    @Test
    void getBookByIdTest_Success() {

        long id = 1L;

        Book book = new Book();
        book.setId(id);

        when(booksRepository.findById(id)).thenReturn(Optional.of(book));

        bookService.getBookById(id);

        verify(booksRepository, times(1)).findById(id);
    }

    @Test
    void getBookByIdTest_NotFound() {

        long id = 1L;

        when(booksRepository.findById(id)).thenReturn(Optional.empty());
        assertNull(bookService.getBookById(id));
        verify(booksRepository, times(1)).findById(id);
    }

    @Test
    void updateBookById_Success() {
        long id = 1L;

        Book existingBook = new Book();
        existingBook.setId(id);
        existingBook.setTitle("Old Title");

        Book updatedBook = new Book();
        updatedBook.setTitle("New Title");

        when(booksRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(booksRepository.save(existingBook)).thenReturn(existingBook);

        BookDTO bookDTO = bookService.updateBookById(id, BookDTO.fromEntity(updatedBook));
        verify(booksRepository, times(1)).findById(id);
        verify(booksRepository, times(1)).save(existingBook);

        assertEquals("New Title", bookDTO.getTitle());
    }

    @Test
    void getAllBook_Success() {

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");

        List<Book> mockList = List.of(book1, book2);

        when(booksRepository.findAll()).thenReturn(mockList);

        List<BookDTO> result = bookService.getAllBook();

        assertNotNull(result);
        assertEquals(mockList.size(), result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());

        verify(booksRepository, times(1)).findAll();
    }

    @Test
    void getBookByIsbn_Success() {

        String isbn = "123";

        Book book = new Book();
        book.setIsbn(isbn);

        when(booksRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        bookService.getBookByIsbn(isbn);

        verify(booksRepository, times(1)).findByIsbn(isbn);
    }

    @Test
    void getBookByIsbn_NotFound() {

        String isbn = "123";

        when(booksRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        assertNull(bookService.getBookByIsbn(isbn));
        verify(booksRepository, times(1)).findByIsbn(isbn);
    }

    @Test
    void getMostPopularBook_Success() {
        Book book1 = new Book(1L);
        Book book2 = new Book(2L);
        book2.setCountOfBorrowingBook(10L);
        book1.setCountOfBorrowingBook(5L);
        List<Book> mockList = List.of(book2, book1);
        when(booksRepository.sortByBookPopularityDescending()).thenReturn(mockList);
        List<BookDTO> dto = List.of(BookDTO.fromEntity(book2), BookDTO.fromEntity(book1));
        List<BookDTO> result = bookService.getMostPopularBook();
        assertEquals(dto, result);
        verify(booksRepository, times(1)).sortByBookPopularityDescending();
    }

    @Test
    void getBookByTitle_Success() {
        Book book1 = new Book();
        book1.setTitle("Book 1");

        when(booksRepository.findBookByTitle(book1.getTitle())).thenReturn(Optional.of(book1));

        BookDTO res = bookService.getBookByTitle(book1.getTitle());
        assertEquals(BookDTO.fromEntity(book1), res);
        verify(booksRepository, times(1)).findBookByTitle(book1.getTitle());
    }
}
