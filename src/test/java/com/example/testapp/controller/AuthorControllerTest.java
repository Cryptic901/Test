package com.example.testapp.controller;

import com.example.testapp.exception.EntityNotFoundException;
import com.example.testapp.model.Author;
import com.example.testapp.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Transactional
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:latest");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        if(authorRepository.findById(3L).isEmpty()) {
            Author author = new Author(3L, "John Doe");
            authorRepository.saveAndFlush(author);
        }
    }
    @Test
    @WithMockUser
    void getAllAuthors() throws Exception {
        var result = mvc.perform(get("/api/v1/authors/getAll"))
                .andExpect(status().isOk())
                .andReturn();

        var authors = authorRepository.findAll();
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(authors));
    }

    @Test
    @WithMockUser
    void getAuthorById() throws Exception {
        long id = 3;
        var result = mvc.perform(get("/api/v1/authors/get/id/{id}", id))
                .andExpect(status().isOk())
                .andReturn();

        var author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(author));
    }
}
