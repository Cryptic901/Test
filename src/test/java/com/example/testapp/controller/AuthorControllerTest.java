package com.example.testapp.controller;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.model.Authors;
import com.example.testapp.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @Test
    @WithMockUser
    void addAuthor() throws Exception {

        //Arrange
        AuthorDTO authorDTO = new AuthorDTO();

        //Act
        when(authorService.addAuthor(any(AuthorDTO.class))).thenReturn(authorDTO);

        //Assert
        mockMvc.perform(post("/api/v1/authors/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void getAuthorById() throws Exception {
        long id = 1L;
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(id);
        when(authorService.getAuthorById(id)).thenReturn(authorDTO);

        mockMvc.perform(get("/api/v1/authors/get/id/{id}", id)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getAllAuthors() throws Exception {
        List<AuthorDTO> authorDTOList = List.of(
                new AuthorDTO(1L),
                new AuthorDTO(1L)
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String exceptedJson = objectMapper.writeValueAsString(authorDTOList);

        when(authorService.getAllAuthors()).thenReturn(authorDTOList);

        mockMvc.perform(get("/api/v1/authors/getAll")
                        .with(csrf())
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(exceptedJson));
    }

    @Test
    @WithMockUser
    void updateAuthorById() throws Exception {
        long id = 1L;
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(id);

        when(authorService.updateAuthorById(id, authorDTO)).thenReturn(authorDTO);
        when(authorService.getAuthorById(id)).thenReturn(authorDTO);

        mockMvc.perform(patch("/api/v1/authors/update/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteAuthor() throws Exception {
        long id = 1L;
        doNothing().when(authorService).deleteAuthorById(id);

        mockMvc.perform(delete("/api/v1/authors/delete/{id}", id)
                        .with(csrf()))
                .andExpect(status().isGone());
    }

    @Test
    @WithMockUser
    void getAllAuthorsBooks() throws Exception {

        long authorId = 1L;

        List<BookShortDTO> bookDtoList = List.of(
                new BookShortDTO(1L, "Book Title 1"),
                new BookShortDTO(2L, "Book Title 2"),
                new BookShortDTO(3L, "Book Title 3")
        );

        when(authorService.getAllAuthorsBooks(authorId)).thenReturn(bookDtoList);
        when(authorService.getAuthorById(authorId)).thenReturn(new AuthorDTO());

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(bookDtoList);

        mockMvc.perform(get("/api/v1/authors/getAllBooks/{authorId}", authorId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser
    void getAuthorByName() throws Exception {
        Authors authors = new Authors();
        authors.setName("Author Name");

        when(authorService.getAuthorByName(authors.getName())).thenReturn(AuthorDTO.fromEntity(authors));

        mockMvc.perform(get("/api/v1/authors/get/name/{name}", authors.getName())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
