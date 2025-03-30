package com.example.testapp.controller;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.SetUpForIntegrationTests;
import com.example.testapp.exception.EntityNotFoundException;
import com.example.testapp.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        User user = userRepository.findUserByUsername("User№2")
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
        User user = userRepository.findUserByUsername("User№3")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        long id = user.getId();
        String exceptedJson = mapper.writeValueAsString(UserDTO.fromEntity(user));

        mvc.perform(get("/api/v1/users/get/id/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(exceptedJson));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUserAllFields_Success() throws Exception {
        User updatedUser = new User();
        updatedUser.setUsername("New Username");
        User testUser = userRepository.findUserByUsername("User№1")
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
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
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUserById_Success() throws Exception {
    }

    @Test
    @WithMockUser
    void borrowBookById_Success() throws Exception {
    }

    @Test
    @WithMockUser
    void returnBookById_Success() throws Exception {
    }

    @Test
    @WithMockUser
    void getBooksThatUserReading_Success() throws Exception {
    }
}