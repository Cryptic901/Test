package com.example.testapp.controller;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.SetUpForIntegrationTests;
import com.example.testapp.exception.EntityNotFoundException;
import com.example.testapp.model.Author;
import com.example.testapp.model.Book;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTest extends SetUpForIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser
    void getAllAuthors_Success() throws Exception {
        var result = mvc.perform(get("/api/v1/authors/getAll"))
                .andExpect(status().isOk())
                .andReturn();

        var authors = authorRepository.findAll().stream()
                .map(AuthorDTO::fromEntity).toList();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(mapper.writeValueAsString(authors));
    }

    @Test
    @WithMockUser
    void getAuthorById_Success() throws Exception {
        var testAuthor = authorRepository.findAuthorByName("author1")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        long id = testAuthor.getId();

        var result = mvc.perform(get("/api/v1/authors/get/id/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        var author = AuthorDTO.fromEntity(authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found")));

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(mapper.writeValueAsString(author));
    }

    @Test
    @WithMockUser
    void getAuthorByName_Success() throws Exception {
        var testAuthor = authorRepository.findAuthorByName("author1")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        String name = testAuthor.getName();

        var result = mvc.perform(get("/api/v1/authors/get/name/{name}", name))
                .andExpect(status().isOk())
                .andReturn();

        var author = AuthorDTO.fromEntity(authorRepository.findAuthorByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Author not found")));

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(mapper.writeValueAsString(author));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createAuthor_Success() throws Exception {
        Author testAuthor = new Author();
        testAuthor.setName("New Author");

        String jsonRequest = mapper.writeValueAsString(testAuthor);

        var result = mvc.perform(post("/api/v1/authors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AuthorDTO authorDTO = mapper.readValue(jsonResponse, AuthorDTO.class);
        assertThat(authorDTO.getName()).isEqualTo(testAuthor.getName());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateAuthorAllFields_Success() throws Exception {
        Author updatedAuthor = new Author();
        updatedAuthor.setName("New Author");
        Author testAuthor = authorRepository.findAuthorByName("author1")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        long id = testAuthor.getId();

        String jsonRequest = mapper.writeValueAsString(updatedAuthor);

        var result = mvc.perform(put("/api/v1/authors/update/allFields/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AuthorDTO authorDTO = mapper.readValue(jsonResponse, AuthorDTO.class);
        assertThat(authorDTO.getName()).isEqualTo(updatedAuthor.getName());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateAuthorDefinedFields_Success() throws Exception {
        Author updatedAuthor = new Author();
        updatedAuthor.setName("New Author");
        updatedAuthor.setBiography("New Biography");
        Author testAuthor = authorRepository.findAuthorByName("author1")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        testAuthor.setBiography("Test Biography");
        long id = testAuthor.getId();

        String jsonRequest = mapper.writeValueAsString(updatedAuthor);

        var result = mvc.perform(patch("/api/v1/authors/update/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AuthorDTO authorDTO = mapper.readValue(jsonResponse, AuthorDTO.class);
        assertThat(authorDTO.getName()).isEqualTo(updatedAuthor.getName());
        assertThat(authorDTO.getBiography()).isNotEqualTo(updatedAuthor.getBiography());
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteAuthorById_Success() throws Exception {
        List<Book> books = bookRepository.findAllById(List.of(1L, 2L, 3L));
        Author testAuthor = authorRepository.findAuthorByName("author2")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        testAuthor.setBookList(books.stream().map(Book::getId).toList());
        long id = testAuthor.getId();

       var result = mvc.perform(delete("/api/v1/authors/delete/{id}", id))
                .andExpect(status().isGone())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
               .isEqualTo("Author deleted successfully");
        assertThat(authorRepository.findById(id).isEmpty()).isTrue();
    }

    @Test
    @WithMockUser
    @Transactional
    void getAllAuthorBooks_Success() throws Exception {
        Author author = authorRepository.findAuthorByName("author2")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        long id = author.getId();

        var result = mvc.perform(get("/api/v1/authors/getAllBooks/{authorId}", id))
                .andExpect(status().isOk())
                .andReturn();

        List<Long> authorBookList = author.getBookList();
        List<BookShortDTO> bookList = bookRepository.findAllById(authorBookList)
                .stream().map(BookShortDTO::fromEntity).toList();
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse)
                .isEqualTo(mapper.writeValueAsString(bookList));
    }
}
