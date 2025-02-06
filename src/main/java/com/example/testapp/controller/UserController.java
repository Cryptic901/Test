package com.example.testapp.controller;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* Контроллер пользователей с которым будет взаимодействовать пользователь */

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserDTO> findById(@PathVariable long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable long userId, UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserById(userId, userDTO));
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }

    @PostMapping("/{bookId}/borrow")
    public ResponseEntity<String> borrowBookById(@PathVariable long bookId, @RequestParam long userId) {
        return ResponseEntity.ok(userService.borrowBookById(bookId, userId));
    }

    @PostMapping("/{bookId}/return")
    public ResponseEntity<String> returnBookById(@PathVariable long bookId, @RequestParam long userId) {
        return ResponseEntity.ok(userService.returnBookById(bookId, userId));
    }
}
