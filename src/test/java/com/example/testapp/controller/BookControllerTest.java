package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.service.BookService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

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
                        .andExpect(status().isOk());
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
                .andExpect(status().isOk());
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
        mockMvc.perform(patch("/api/v1/books/update/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
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
    void getAllBooksTest() throws Exception {

        //Arrange
        List<BookDTO> bookDTOList = new ArrayList<>(List.of(new BookDTO(), new BookDTO()));

        //Act
        when(bookService.getAllBooks()).thenReturn(bookDTOList);

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
}
