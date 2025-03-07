package com.example.testapp.service;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    void setUserParams(User user, UserDTO userDTO);

    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUser();

    UserDTO getUserByUsername(String username);

    UserDTO getUserById(long id);

    UserDTO updateUserById(long id, UserDTO userDTO);

    void deleteUserById(long userId);

    String borrowBookById(long bookId, long userId);

    String returnBookById(long bookId, long userId);

    UserDTO updateUserFields(long id, Map<String, Object> updates);
}
