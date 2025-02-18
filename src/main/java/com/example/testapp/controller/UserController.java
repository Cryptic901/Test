package com.example.testapp.controller;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* Контроллер пользователей с которым будет взаимодействовать пользователь */

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Пользователи", description = "Методы работы с пользователями")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @Operation(summary = "Создать пользователя", description = "Создаёт пользователя")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping("/getAll")
    @Operation(summary = "Вернуть всех пользователей", description = "Возвращает список всех пользователей")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/get/username/{username}")
    @Operation(summary = "Получить пользователя по имени", description = "Возвращает пользователя у которого имя совпадает с введенным")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/get/id/{userId}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя у которого ID совпадает с введенным")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/update/{userId}")
    @Operation(summary = "Обновить пользователя по ID", description = "Обновляет параметры пользователя ID которого введено")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable long userId,@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserById(userId, userDTO));
    }

    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "Удалить пользователя по ID", description = "Удаляет пользователя по ID")
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }

    @PostMapping("/{bookId}/borrow")
    @Operation(summary = "Занять книгу из библиотеки",
            description = "Занимает книгу из библиотеки, изменяет статус книги на BORROWED и добавляет в список занятых книг пользователем")
    public ResponseEntity<String> borrowBookById(@PathVariable long bookId, @RequestParam long userId) {
        return ResponseEntity.ok(userService.borrowBookById(bookId, userId));
    }

    @PostMapping("/{bookId}/return")
    @Operation(summary = "Вернуть книгу в библиотеку",
            description = "Возвращает книгу в библиотеку, изменяет статус книги на AVAILABLE и удаляет из списка занятых книг пользователем")
    public ResponseEntity<String> returnBookById(@PathVariable long bookId, @RequestParam long userId) {
        return ResponseEntity.ok(userService.returnBookById(bookId, userId));
    }
}
