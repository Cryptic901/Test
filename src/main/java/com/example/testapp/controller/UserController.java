package com.example.testapp.controller;

import com.example.testapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/* Контроллер пользователей с которым будет взаимодействовать пользователь */

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("public/users/{bookId}/borrow")
    public ResponseEntity<String> borrowBookById(@PathVariable long bookId, @RequestParam long userId) {
        return ResponseEntity.ok(userService.borrowBookById(bookId, userId));
    }

    @PostMapping("public/users/{bookId}/return")
    public ResponseEntity<String> returnBookById(@PathVariable long bookId, @RequestParam long userId) {
        return ResponseEntity.ok(userService.returnBookById(bookId, userId));
    }
}
