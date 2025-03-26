package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.SetUpForIntegrationTests;
import com.example.testapp.exception.EntityNotFoundException;
import com.example.testapp.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest extends SetUpForIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBookById_Success() throws Exception {
        List<Book> listOfBooks = bookRepository.findAll();
        long idToDelete = listOfBooks.get(1).getId();
        int sizeOfBooks = listOfBooks.size();
        var result = mvc.perform(delete("/api/v1/books/delete/{id}", idToDelete))
                .andExpect(status().isGone())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Book deleted successfully");
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(sizeOfBooks - 1);
        assertThat(bookRepository.findById(idToDelete)).isEmpty();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void addBook_Success() throws Exception {
        Book testBook = new Book();
        testBook.setTitle("Test Book42");
        testBook.setIsbn("123456789011111");
        testBook.setPublishedDate(LocalDate.now());
        testBook.setPublisher("Test Publisher21");
        testBook.setCountOfBorrowingBook(14L);
        testBook.setAuthor(authorRepository.findAuthorByName("author1").orElse(null));
        testBook.setGenre(genreRepository.findByName("genre1").orElse(null));

        String jsonRequest = mapper.writeValueAsString(testBook);

        var result = mvc.perform(post("/api/v1/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookDTO bookDTO = mapper.readValue(jsonResponse, BookDTO.class);
        assertThat(bookDTO.getTitle()).isEqualTo(testBook.getTitle());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void updateBookAllFields_Success() throws Exception {
        Book testBook = new Book();
        testBook.setTitle("Test Book42");
        testBook.setIsbn("123456789011111");
        testBook.setPublishedDate(LocalDate.now());
        testBook.setPublisher("Test Publisher21");
        testBook.setCountOfBorrowingBook(14L);
        testBook.setAuthor(authorRepository.findAuthorByName("author1").orElse(null));
        testBook.setGenre(genreRepository.findByName("genre1").orElse(null));

        Book bookToUpdate = bookRepository.findBookByTitle("Test Book3")
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        long idToUpdate = bookToUpdate.getId();
        String jsonRequest = mapper.writeValueAsString(testBook);

        var result = mvc.perform(put("/api/v1/books/update/allFields/{id}", idToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookDTO bookDTO = mapper.readValue(jsonResponse, BookDTO.class);
        assertThat(bookDTO.getTitle()).isEqualTo(testBook.getTitle());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBook_Success() throws Exception {
        Book testBook = bookRepository.findBookByTitle("Test Book2")
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("title", "New TestBook42");
        String jsonRequest = mapper.writeValueAsString(updateRequest);
        long id = testBook.getId();

        var result = mvc.perform(patch("/api/v1/books/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookDTO bookDTO = mapper.readValue(jsonResponse, BookDTO.class);
        assertThat(bookDTO.getTitle()).isEqualTo("New TestBook42");
    }

    @Test
    @WithMockUser
    void getBookById_Success() throws Exception {
        Book testBook = bookRepository.findBookByTitle("Test Book")
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        long bookId = testBook.getId();

        var result = mvc.perform(get("/api/v1/books/get/id/{id}", bookId))
                .andExpect(status().isOk())
                .andReturn();

        var book = BookDTO.fromEntity(bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found")));

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(mapper.writeValueAsString(book));
    }

    @Test
    @WithMockUser
    @Transactional
    void getAllBooks_Success() throws Exception {

        List<Book> books = bookRepository.findAll();
        String expectedJson = mapper.writeValueAsString(books
                .stream().map(BookDTO::fromEntity).toList());
        mvc.perform(get("/api/v1/books/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser
    void getAllBookShort_Success() throws Exception {

        List<BookShortDTO> books = bookRepository.findAll().stream()
                .map(BookShortDTO::fromEntity).toList();
        String expectedJson = mapper.writeValueAsString(books);
        mvc.perform(get("/api/v1/books/getAll/short"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser
    void getBookByIsbn_Success() throws Exception {
        Book testBook = bookRepository.findBookByTitle("Test Book")
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        String bookIsbn = testBook.getIsbn();

        var result = mvc.perform(get("/api/v1/books/get/isbn/{isbn}", bookIsbn))
                .andExpect(status().isOk())
                .andReturn();

        var book = BookDTO.fromEntity(bookRepository.findByIsbn(bookIsbn)
                .orElseThrow(() -> new EntityNotFoundException("Book not found")));

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(mapper.writeValueAsString(book));
    }

    @Test
    @WithMockUser
    void getBookByTitle_Success() throws Exception {
        Book testBook = bookRepository.findByIsbn("3234567890")
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        String bookTitle = testBook.getTitle();

        var result = mvc.perform(get("/api/v1/books/get/title/{title}", bookTitle))
                .andExpect(status().isOk())
                .andReturn();

        var book = BookDTO.fromEntity(bookRepository.findBookByTitle(bookTitle)
                .orElseThrow(() -> new EntityNotFoundException("Book not found")));

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(mapper.writeValueAsString(book));
    }

    @Test
    @WithMockUser
    @Transactional
    void getPopularityDesc_Success() throws Exception {
        List<Book> books = bookRepository.findAll();
        List<Book> exceptedList = List.of(
                books.get(3),
                books.get(2),
                books.get(0),
                books.get(1));
        String expectedJson = mapper.writeValueAsString(exceptedList
                .stream().map(BookDTO::fromEntity).toList());

        mvc.perform(get("/api/v1/books/sort/popularityDesc"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}