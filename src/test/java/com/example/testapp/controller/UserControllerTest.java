package com.example.testapp.controller;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.service.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

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
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getAllUsersTest() throws Exception {

        //Arrange
        List<UserDTO> userDTOList = new ArrayList<>();

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

        //Act
        when(userService.getUserByUsername("Cryptic")).thenReturn(userDTO);

        //Assert
        mockMvc.perform(get("/api/v1/users/Cryptic")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getUserByIdTest() throws Exception {

        //Arrange
        UserDTO userDTO = new UserDTO();

        //Act
        when(userService.getUserById(eq(1L))).thenReturn(userDTO);

        //Assert
        mockMvc.perform(get("/api/v1/users/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateUserByIdTest() throws Exception {

        //Arrange
        UserDTO userDTO = new UserDTO();

        //Act
        when(userService.updateUserById(1L, userDTO)).thenReturn(userDTO);

        //Assert
        mockMvc.perform(patch("/api/v1/users/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteUserByIdTest() throws Exception {

        //Act
        doNothing().when(userService).deleteUserById(eq(1L));

        //Assert
        mockMvc.perform(delete("/api/v1/users/delete/1")
                        .with(csrf()))
                .andExpect(status().isOk());
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

        //Assert
        mockMvc.perform(post("/api/v1/users/2/borrow")
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

        //Assert
        mockMvc.perform(post("/api/v1/users/2/return")
        .with(csrf())
                .param("userId", String.valueOf(userId))
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));

    }
}
