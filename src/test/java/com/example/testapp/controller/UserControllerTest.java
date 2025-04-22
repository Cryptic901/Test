package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.UserDTO;
import com.example.testapp.SetUpForIntegrationTests;
import com.example.testapp.exception.EntityNotFoundException;
import com.example.testapp.model.Book;
import com.example.testapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends SetUpForIntegrationTests {

    @Autowired
    private MockMvc mvc;


    @Test
    @WithMockUser
    @Transactional
    void getAllUser_Success() throws Exception {
        List<User> exceptedUsers = userRepository.findAll();
        String exceptedJson = mapper.writeValueAsString(exceptedUsers
                .stream().map(UserDTO::fromEntity).toList());

        mvc.perform(get("/api/v1/users/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().json(exceptedJson));
    }

    @Test
    @WithMockUser
    @Transactional
    void getUserByUsername_Success() throws Exception {
        User user = userRepository.findByEmail("user2@gmail.com")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String username = user.getUsername();
        String exceptedJson = mapper.writeValueAsString(UserDTO.fromEntity(user));

        mvc.perform(get("/api/v1/users/get/username/{username}", username))
                .andExpect(status().isOk())
                .andExpect(content().json(exceptedJson));
    }

    @Test
    @WithMockUser
    @Transactional
    void getUserByEmail_Success() throws Exception {
        User user = userRepository.findUserByUsername("User2")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String email = user.getEmail();
        String exceptedJson = mapper.writeValueAsString(UserDTO.fromEntity(user));

        mvc.perform(get("/api/v1/users/get/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().json(exceptedJson));
    }

    @Test
    @WithMockUser
    @Transactional
    void getUserById_Success() throws Exception {
        User user = userRepository.findUserByUsername("User3")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        long id = user.getId();
        String exceptedJson = mapper.writeValueAsString(UserDTO.fromEntity(user));

        mvc.perform(get("/api/v1/users/get/id/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(exceptedJson));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void updateUserAllFields_Success() throws Exception {
        User testUser = userRepository.findUserByUsername("User1")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        User updatedUser = userRepository.findUserByUsername("User1")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        updatedUser.setUsername("User4");
        long id = testUser.getId();

        String jsonRequest = mapper.writeValueAsString(UserDTO.fromEntity(updatedUser));

        var result = mvc.perform(put("/api/v1/users/update/allFields/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        UserDTO userDTO = mapper.readValue(jsonResponse, UserDTO.class);
        assertThat(userDTO.getUsername()).isEqualTo(updatedUser.getUsername());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_Success() throws Exception {
        User testUser = userRepository.findUserByUsername("User1")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("username", "User1Updated");
        long id = testUser.getId();

        String jsonRequest = mapper.writeValueAsString(updatedUser);

        var result = mvc.perform(patch("/api/v1/users/update/{id}", id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        UserDTO userDTO = mapper.readValue(jsonResponse, UserDTO.class);
        System.out.println(userDTO);
        assertThat(userDTO.getUsername()).isEqualTo(updatedUser.get("username"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void deleteUserById_Success() throws Exception {
        User user = userRepository.findUserByUsername("User1")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        long id = user.getId();

        var result = mvc.perform(delete("/api/v1/users/delete/{userId}", id))
                .andExpect(status().isGone())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("User deleted successfully");
        assertThat(userRepository.findById(id).isEmpty()).isTrue();
    }

    @Test
    @WithMockUser(username = "user2@gmail.com")
    @Transactional
    void borrowBookById_Success() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        borrowBook("Test Book3",auth.getName());
        User user = userRepository.findByEmail(auth.getName())
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));
        assertThat(user.getBorrowedBooks().isEmpty()).isFalse();
    }

    @Test
    @WithMockUser
    @Transactional
    void getBooksThatUserReading_Success() throws Exception {
        User user = userRepository.findUserByUsername("User1")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        long id = user.getId();
        List<BookDTO> books = bookRepository.getBooksByBorrowedUserIds(id).stream()
                .map(BookDTO::fromEntity).toList();

        String exceptedResponse = mapper.writeValueAsString(books);

        System.out.println(exceptedResponse);
        mvc.perform(get("/api/v1/users/reading")
                        .param("userId", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().json(exceptedResponse));
    }

    protected void borrowBook(String bookTitle, String email) throws Exception {
        Book book = bookRepository.findBookByTitle(bookTitle)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        long bookId = book.getId();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setEnabled(true);
        mvc.perform(post("/api/v1/users/borrow")
                        .param("bookId", String.valueOf(bookId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book borrowed successfully!"));
    }
}