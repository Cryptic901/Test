package com.example.testapp.controller;

import com.example.testapp.DTO.UserDTO;
import com.example.testapp.service.impl.BookServiceImpl;
import com.example.testapp.service.impl.UserServiceImpl;
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

    @PostMapping("/create")
    @Operation(summary = "Создать пользователя",
            description = "Создаёт пользователя, при неверном введении отправляет статус 400")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }

    @GetMapping("/getAll")
    @Operation(summary = "Вернуть всех пользователей",
            description = "Возвращает список всех пользователей, если список пустой статус 204")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        if (userService.getAllUser().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/get/username/{username}")
    @Operation(summary = "Получить пользователя по имени",
            description = "Возвращает пользователя у которого имя совпадает с введенным," +
                    " если пользователь не найден статус 204")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        if (userService.getUserByUsername(username) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/get/id/{userId}")
    @Operation(summary = "Получить пользователя по ID",
            description = "Возвращает пользователя у которого ID совпадает с введенным," +
                    " если пользователь не найден статус 204")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long userId) {
        if (userService.getUserById(userId) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/update/allFields/{userId}")
    @Operation(summary = "Обновить пользователя по ID",
            description = "Обновляет пользователя ID которого введено," +
                    " если пользователь не найден статус 204, при неверном введении статус 400")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable long userId,@RequestBody UserDTO userDTO) {
        if (userService.getUserById(userId) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userService.updateUserById(userId, userDTO));
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Обновить определенные поля пользователя по ID",
            description = "Обновляет поля которые были введены для пользователя с введенным ID," +
                    " если пользователь не найден статус 204, при неверном вводе статус 400")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @RequestBody Map<String, Object> updates) {
        if (userService.getUserById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if (updates == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserDTO userDTO = userService.updateUserFields(id, updates);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "Удалить пользователя по ID",
            description = "Удаляет пользователя по ID отправляя статус 410, если не находит по id статус 204")
    public ResponseEntity<UserDTO> deleteUserById(@PathVariable long userId) {
        if (userService.getUserById(userId) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @PostMapping("/{bookId}/borrow")
    @Operation(summary = "Занять книгу из библиотеки",
            description = "Занимает книгу из библиотеки," +
                    " изменяет статус книги на BORROWED и добавляет в список занятых книг пользователем," +
                    " если не находит статус 204")
    public ResponseEntity<String> borrowBookById(@PathVariable long bookId, @RequestParam long userId) {
        if (userService.getUserById(userId) == null || bookService.getBookById(bookId) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userService.borrowBookById(bookId, userId));
    }

    @PostMapping("/{bookId}/return")
    @Operation(summary = "Вернуть книгу в библиотеку",
            description = "Возвращает книгу в библиотеку," +
                    " изменяет статус книги на AVAILABLE и удаляет из списка занятых книг пользователем," +
                    " если не находит статус 204")
    public ResponseEntity<String> returnBookById(@PathVariable long bookId, @RequestParam long userId) {
        if (userService.getUserById(userId) == null || bookService.getBookById(bookId) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(userService.returnBookById(bookId, userId));
    }
}
