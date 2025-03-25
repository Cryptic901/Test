package com.example.testapp.controller;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.exception.EntityNotFoundException;
import com.example.testapp.model.Author;
import com.example.testapp.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Container
    @ServiceConnection
    private final static PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:latest");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
        authorRepository.flush();
        List<BookShortDTO> books = List.of(
                new BookShortDTO(1L, "book1"),
                new BookShortDTO(2L, "book2"),
                new BookShortDTO(3L, "book3")
        );
        Author testAuthor = new Author();
        testAuthor.setName("Test Author");
        authorRepository.saveAndFlush(testAuthor);
        Author testAuthor2 = new Author();
        testAuthor2.setName("Test Author2");
        authorRepository.saveAndFlush(testAuthor2);
        Author testAuthor3 = new Author();
        testAuthor3.setName("Test Author3");
        testAuthor3.setBookList(books.stream().map(BookShortDTO::getId).toList());
        authorRepository.saveAndFlush(testAuthor3);
        Author testAuthor4 = new Author();
        testAuthor4.setName("Test Author4");
        authorRepository.saveAndFlush(testAuthor4);
    }

    @Test
    @WithMockUser
    void getAllAuthors_Success() throws Exception {
        var result = mvc.perform(get("/api/v1/authors/getAll"))
                .andExpect(status().isOk())
                .andReturn();

        var authors = authorRepository.findAll().stream()
                .map(AuthorDTO::fromEntity).toList();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(authors));
    }

    @Test
    @WithMockUser
    void getAuthorById_Success() throws Exception {
        var testAuthor = authorRepository.findAuthorByName("Test Author")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        long id = testAuthor.getId();

        var result = mvc.perform(get("/api/v1/authors/get/id/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        var author = AuthorDTO.fromEntity(authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found")));

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(author));
    }

    @Test
    @WithMockUser
    void getAuthorByName_Success() throws Exception {
        var testAuthor = authorRepository.findAuthorByName("Test Author")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        String name = testAuthor.getName();

        var result = mvc.perform(get("/api/v1/authors/get/name/{name}", name))
                .andExpect(status().isOk())
                .andReturn();

        var author = AuthorDTO.fromEntity(authorRepository.findAuthorByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Author not found")));

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(author));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createAuthor_Success() throws Exception {
        Author testAuthor = new Author();
        testAuthor.setName("New Author");

        String jsonRequest = objectMapper.writeValueAsString(testAuthor);

        var result = mvc.perform(post("/api/v1/authors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AuthorDTO authorDTO = objectMapper.readValue(jsonResponse, AuthorDTO.class);
        assertThat(authorDTO.getName()).isEqualTo(testAuthor.getName());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateAuthorAllFields_Success() throws Exception {
        Author updatedAuthor = new Author();
        updatedAuthor.setName("New Author");
        Author testAuthor = authorRepository.findAuthorByName("Test Author")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        long id = testAuthor.getId();

        String jsonRequest = objectMapper.writeValueAsString(updatedAuthor);

        var result = mvc.perform(put("/api/v1/authors/update/allFields/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AuthorDTO authorDTO = objectMapper.readValue(jsonResponse, AuthorDTO.class);
        assertThat(authorDTO.getName()).isEqualTo(updatedAuthor.getName());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateAuthorDefinedFields_Success() throws Exception {
        Author updatedAuthor = new Author();
        updatedAuthor.setName("New Author");
        updatedAuthor.setBiography("New Biography");
        Author testAuthor = authorRepository.findAuthorByName("Test Author")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        testAuthor.setBiography("Test Biography");
        long id = testAuthor.getId();

        String jsonRequest = objectMapper.writeValueAsString(updatedAuthor);

        var result = mvc.perform(patch("/api/v1/authors/update/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        AuthorDTO authorDTO = objectMapper.readValue(jsonResponse, AuthorDTO.class);
        assertThat(authorDTO.getName()).isEqualTo(updatedAuthor.getName());
        assertThat(authorDTO.getBiography()).isNotEqualTo(updatedAuthor.getBiography());
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteAuthorById_Success() throws Exception {
        Author testAuthor = authorRepository.findAuthorByName("Test Author2")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        long id = testAuthor.getId();

        mvc.perform(delete("/api/v1/authors/delete/{id}", id))
                .andExpect(status().isGone());

        assertThat(authorRepository.findById(id).isEmpty()).isTrue();
    }

    @Test
    @WithMockUser
    @Transactional
    void getAllAuthorBooks_Success() throws Exception {
        Author author = authorRepository.findAuthorByName("Test Author3")
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        long id = author.getId();

        var result = mvc.perform(get("/api/v1/authors/getAllBooks/{authorId}", id))
                .andExpect(status().isOk())
                .andReturn();

        List<Long> authorBookList = author.getBookList();
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(authorBookList));
    }
}
