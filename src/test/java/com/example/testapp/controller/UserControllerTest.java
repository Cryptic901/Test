package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.UserDTO;
import com.example.testapp.service.impl.BookServiceImpl;
import com.example.testapp.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserServiceImpl userService;

    @MockitoBean
    private BookServiceImpl bookService;

    @InjectMocks
    private UserController userController;


    @Test
    @WithMockUser
    void createUserTest() throws Exception {

        //Arrange
        UserDTO userDTO = new UserDTO();

        //Act
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        //Assert
        mockMvc.perform(post("/api/v1/users/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void getAllUsersTest() throws Exception {

        //Arrange
        List<UserDTO> userDTOList = List.of(
                new UserDTO(),
                new UserDTO()
        );

        //Act
        when(userService.getAllUsers()).thenReturn(userDTOList);

        //Assert
        mockMvc.perform(get("/api/v1/users/getAll")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getUserByUsernameTest() throws Exception {

        //Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Cryptic");

        //Act
        when(userService.getUserByUsername(userDTO.getUsername())).thenReturn(userDTO);

        //Assert
        mockMvc.perform(get("/api/v1/users/get/username/{username}", userDTO.getUsername())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("Cryptic"));
    }

    @Test
    @WithMockUser
    void getUserByIdTest() throws Exception {

        //Arrange
        long id = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);

        //Act
        when(userService.getUserById(1L)).thenReturn(userDTO);

        //Assert
        mockMvc.perform(get("/api/v1/users/get/id/{id}", id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void updateUserByIdTest() throws Exception {

        //Arrange
        long id = 1L;
        UserDTO userDTO = new UserDTO();

        //Act
        when(userService.updateUserById(id, userDTO)).thenReturn(userDTO);
        when(userService.getUserById(id)).thenReturn(userDTO);

        //Assert
        mockMvc.perform(put("/api/v1/users/update/allFields/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateUserTest() throws Exception {

        //Arrange
        long id = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Cryptik");
        Map<String, Object> map = new HashMap<>();
        map.put("username", "Cryptic");

        //Act
        when(userService.updateUserFields(eq(id), anyMap())).thenAnswer(invocation -> {
            userDTO.setUsername("Cryptic");
            return userDTO;
        });
        when(userService.getUserById(id)).thenReturn(userDTO);

        //Assert
        mockMvc.perform(patch("/api/v1/users/update/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Cryptic"));
        verify(userService, times(1)).updateUserFields(eq(id), anyMap());
    }

    @Test
    @WithMockUser
    void deleteUserByIdTest() throws Exception {

        //Arrange
        long id = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);

        //Act
        when(userService.getUserById(id)).thenReturn(userDTO);
        doNothing().when(userService).deleteUserById(userDTO.getId());

        //Assert
        mockMvc.perform(delete("/api/v1/users/delete/{id}", id)
                        .with(csrf()))
                .andExpect(status().isGone());
    }

    @Test
    @WithMockUser
    void borrowBookByIdTest() throws Exception {

        //Arrange
        long userId = 1L;
        long bookId = 2L;
        String successMessage = "Book borrowed successfully!";

        //Act
        when(userService.borrowBookById(bookId, userId)).thenReturn(successMessage);
        when(userService.getUserById(userId)).thenReturn(new UserDTO());
        when(bookService.getBookById(bookId)).thenReturn(new BookDTO());

        //Assert
        mockMvc.perform(post("/api/v1/users/{bookId}/borrow", bookId)
                        .with(csrf())
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));
    }

    @Test
    @WithMockUser
    void returnBookByIdTest() throws Exception {

        //Arrange
        long userId = 1L;
        long bookId = 2L;
        String successMessage = "Book returned successfully!";

        //Act
        when(userService.returnBookById(bookId, userId)).thenReturn(successMessage);
        when(userService.getUserById(userId)).thenReturn(new UserDTO());
        when(bookService.getBookById(bookId)).thenReturn(new BookDTO());

        //Assert
        mockMvc.perform(post("/api/v1/users/{bookId}/return", bookId)
        .with(csrf())
                .param("userId", String.valueOf(userId))
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));

    }
}
