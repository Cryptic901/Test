package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.UserDTO;
import com.example.testapp.impl.BookServiceImpl;
import com.example.testapp.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/* Контроллер пользователей с которым будет взаимодействовать пользователь */

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Пользователи",
        description = "Методы работы с пользователями")
public class UserController {

    private final UserServiceImpl userService;
    private final BookServiceImpl bookService;

    @Autowired
    public UserController(UserServiceImpl userService, BookServiceImpl bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/getAll")
    @Operation(summary = "Вернуть всех пользователей",
            description = "Возвращает список всех пользователей, если список пустой статус 204")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        boolean notHaveAnyUser = userService.getAllUser().isEmpty();
        if (notHaveAnyUser) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/get/username/{username}")
    @Operation(summary = "Получить пользователя по имени",
            description = "Возвращает пользователя у которого имя совпадает с введенным," +
                    " если пользователь не найден статус 204")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return user == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(user);
    }

    @GetMapping("/get/id/{userId}")
    @Operation(summary = "Получить пользователя по ID",
            description = "Возвращает пользователя у которого ID совпадает с введенным," +
                    " если пользователь не найден статус 204")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long userId) {
        UserDTO user = userService.getUserById(userId);
        return user == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(user);
    }

    @PutMapping("/update/allFields/{userId}")
    @Operation(summary = "Обновить пользователя по ID",
            description = "Обновляет пользователя ID которого введено," +
                    " если пользователь не найден статус 204, при неверном введении статус 400")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable long userId, @RequestBody UserDTO userDTO) {
        UserDTO user = userService.getUserById(userId);
        UserDTO updatedUser = userService.updateUserById(userId, userDTO);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Обновить определенные поля пользователя по ID",
            description = "Обновляет поля которые были введены для пользователя с введенным ID," +
                    " если пользователь не найден статус 204, при неверном вводе статус 400")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody Map<String, Object> updates) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (updates == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserDTO updatedUser = userService.updateUserFields(id, updates);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "Удалить пользователя по ID",
            description = "Удаляет пользователя по ID отправляя статус 410, если не находит по id статус 204")
    public ResponseEntity<UserDTO> deleteUserById(@PathVariable long userId) {
        String response = userService.deleteUserById(userId);
        if (response.equals("User not found")) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @PostMapping("/borrow")
    @Operation(summary = "Занять книгу из библиотеки",
            description = "Занимает книгу из библиотеки," +
                    " изменяет статус книги на BORROWED и добавляет в список занятых книг пользователем," +
                    " если не находит статус 204")
    public ResponseEntity<String> borrowBookById(@RequestParam long bookId) {
        BookDTO book = bookService.getBookById(bookId);
        String borrowResponse = userService.borrowBookById(bookId);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (borrowResponse.equals("To many borrowed books")) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(borrowResponse);
    }

    @PostMapping("/return")
    @Operation(summary = "Вернуть книгу в библиотеку",
            description = "Возвращает книгу в библиотеку," +
                    " изменяет статус книги на AVAILABLE и удаляет из списка занятых книг пользователем," +
                    " если не находит статус 204")
    public ResponseEntity<String> returnBookById(@RequestParam long bookId) {
        BookDTO book = bookService.getBookById(bookId);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userService.returnBookById(bookId));
    }
}
