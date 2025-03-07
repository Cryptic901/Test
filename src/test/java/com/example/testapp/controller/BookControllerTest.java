package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.model.Book;
import com.example.testapp.service.impl.BookServiceImpl;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookServiceImpl bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    @WithMockUser
    void deleteBookByIdTest() throws Exception {

        //Arrange
        long id = 1L;

        //Act
        doNothing().when(bookService).deleteBookById(1L);

        //Assert
        mockMvc.perform(delete("/api/v1/books/delete/{id}", id)
                        .with(csrf()))
                .andExpect(status().isGone());
    }

    @Test
    @WithMockUser
    void addBookTest() throws Exception {

        //Arrange
        BookDTO bookDTO = new BookDTO();

        //Act
        when(bookService.addBook(any(BookDTO.class))).thenReturn(bookDTO);

        //Assert
        mockMvc.perform(post("/api/v1/books/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void updateBookByIdTest() throws Exception {

        //Arrange
        long id = 1L;
        BookDTO bookDTO = new BookDTO();

        //Act
        when(bookService.updateBookById(eq(1L), any(BookDTO.class))).thenReturn(bookDTO);

        //Assert
        mockMvc.perform(put("/api/v1/books/update/allFields/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateBookTest() throws Exception {

        //Arrange
        long id = 1L;
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("title");
        Map<String, Object> map = new HashMap<>();
        map.put("title", "new title");

        when(bookService.getBookById(eq(id))).thenReturn(bookDTO);
        when(bookService.updateBookFields(eq(id), anyMap())).thenAnswer(invocation -> {
            bookDTO.setTitle("new title");
            return bookDTO;
        });

        //Assert
        mockMvc.perform(patch("/api/v1/books/update/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("new title"));

        verify(bookService, times(1)).updateBookFields(eq(id), anyMap());
    }

    @Test
    @WithMockUser
    void getBookByIdTest() throws Exception {

        //Arrange
        long id = 1L;
        BookDTO bookDTO = new BookDTO();

        //Act
        when(bookService.getBookById(1L)).thenReturn(bookDTO);

        //Assert
        mockMvc.perform(get("/api/v1/books/get/id/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getAllBookTest() throws Exception {

        //Arrange
        List<BookDTO> bookDTOList = new ArrayList<>(List.of(new BookDTO(), new BookDTO()));

        //Act
        when(bookService.getAllBook()).thenReturn(bookDTOList);

        //Assert
        mockMvc.perform(get("/api/v1/books/getAll"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getBookByIsbnTest() throws Exception {

        //Arrange
        BookDTO bookDTO = new BookDTO();
        String isbn = "123";

        //Act
        when(bookService.getBookByIsbn(isbn)).thenReturn(bookDTO);

        //Assert
        mockMvc.perform(get("/api/v1/books/get/isbn/{isbn}", isbn))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getBookByTitleTest() throws Exception {
        Book books = new Book();
        books.setTitle("title");

        when(bookService.getBookByTitle(books.getTitle())).thenReturn(BookDTO.fromEntity(books));

        mockMvc.perform(get("/api/v1/books/get/title/{title}", books.getTitle())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
