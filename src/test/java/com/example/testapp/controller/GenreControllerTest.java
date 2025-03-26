package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.SetUpForIntegrationTests;
import com.example.testapp.exception.EntityNotFoundException;
import com.example.testapp.model.Book;
import com.example.testapp.model.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class GenreControllerTest extends SetUpForIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void addGenre_Success() throws Exception {
        Genre testGenre = new Genre();
        testGenre.setName("New Genre");

        String jsonRequest = mapper.writeValueAsString(testGenre);

        var result = mvc.perform(post("/api/v1/authors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        GenreDTO genreDTO = mapper.readValue(jsonResponse, GenreDTO.class);
        assertThat(genreDTO.getName()).isEqualTo(testGenre.getName());
    }

    @Test
    @WithMockUser
    @Transactional
    void getAllGenre_Success() throws Exception {
        List<Genre> genres = genreRepository.findAll();

        String json = mapper.writeValueAsString(genres
                .stream().map(GenreDTO::fromEntity).toList());

        mvc.perform(get("/api/v1/genres/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @WithMockUser
    @Transactional
    void getGenreById_Success() throws Exception {
        Genre genre = genreRepository.findByName("genre1")
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        long id = genre.getId();

        String json = mapper.writeValueAsString(GenreDTO.fromEntity(genre));

        mvc.perform(get("/api/v1/genres/get/id/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(json));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGenreAllFields_Success() throws Exception {
        Genre updatedGenre = new Genre();
        updatedGenre.setName("New Genre");
        Genre testGenre = genreRepository.findByName("genre1")
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        long id = testGenre.getId();

        String jsonRequest = mapper.writeValueAsString(updatedGenre);

        var result = mvc.perform(put("/api/v1/genres/update/allFields/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        GenreDTO genreDTO = mapper.readValue(jsonResponse, GenreDTO.class);
        assertThat(genreDTO.getName()).isEqualTo(updatedGenre.getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateGenre_Success() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("description", "New Description");
        Genre testGenre = genreRepository.findByName("genre1")
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        testGenre.setDescription("Test Description");
        long id = testGenre.getId();

        String jsonRequest = mapper.writeValueAsString(params);

        var result = mvc.perform(patch("/api/v1/genres/update/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        GenreDTO genreDTO = mapper.readValue(jsonResponse, GenreDTO.class);
        assertThat(genreDTO.getDescription()).isEqualTo(params.get("description"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteGenre_Success() throws Exception {
        List<Book> books = bookRepository.findAllById(List.of(1L, 2L, 3L));
        Genre testGenre = genreRepository.findByName("genre2")
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        testGenre.setBooks(books.stream().map(Book::getId).toList());
        long id = testGenre.getId();

        var result = mvc.perform(delete("/api/v1/genres/delete/{id}", id))
                .andExpect(status().isGone())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("Genre deleted successfully");
        assertThat(genreRepository.findById(id).isEmpty()).isTrue();
    }

    @Test
    @WithMockUser
    @Transactional
    void getGenreByName_Success() throws Exception {
        Genre genre = genreRepository.findGenreByDescription("genre desc2")
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        String name = genre.getName();
        String json = mapper.writeValueAsString(GenreDTO.fromEntity(genre));

        mvc.perform(get("/api/v1/genres/get/name/{name}", name))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @WithMockUser
    @Transactional
    void getPopularityDesc_Success() throws Exception {
        List<Genre> genres = genreRepository.findAll();
        List<Genre> exceptedList = List.of(
                genres.get(1),
                genres.get(2),
                genres.get(0));
        String expectedJson = mapper.writeValueAsString(exceptedList
                .stream().map(GenreDTO::fromEntity).toList());

        mvc.perform(get("/api/v1/genres/sort/popularityDesc"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser
    @Transactional
    void getAllBooksInThatGenre_Success() throws Exception {
        Genre genre = genreRepository.findByName("genre3")
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        long genreId = genre.getId();
        var result = mvc.perform(get("/api/v1/genres/getAllBooks")
                        .param("genreId", String.valueOf(genreId)))
                .andExpect(status().isOk())
                .andReturn();

        List<Long> bookList = genre.getBooks();
        System.out.println(genre.getBooks().toString());
        System.out.println(bookRepository.findById(genre.getBooks().get(0)));
        System.out.println(bookRepository.findAll());
        Book exceptedBook = bookRepository.findById(bookList.get(0))
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        List<BookDTO> exceptedGenreList = List.of(BookDTO.fromEntity(exceptedBook));
        String json = mapper.writeValueAsString(exceptedGenreList);
        String jsonResponse = result.getResponse().getContentAsString();
        assertThat(jsonResponse)
                .isEqualTo(json);
    }
}