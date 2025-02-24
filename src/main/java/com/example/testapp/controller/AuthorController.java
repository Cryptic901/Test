package com.example.testapp.controller;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/* Контроллер авторов с которым будет взаимодействовать пользователь */

@RestController
@RequestMapping("api/v1/authors")
@Tag(name = "Авторы",
        description = "Методы для работы с авторами")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/create")
    @Operation(summary = "Создать автора",
            description = "Создаёт автора, при неверном введении отправляет статус 400")
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody AuthorDTO authorDTO) {
        if (authorDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.addAuthor(authorDTO));
    }

    @GetMapping("/get/name/{name}")
    @Operation(summary = "Получить автора по имени",
            description = "Возвращает автора с именем совпадающим с введенным, если автор не найден статус 204")
    public ResponseEntity<AuthorDTO> getAuthorByName(@PathVariable String name) {
        if (authorService.getAuthorByName(name) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(authorService.getAuthorByName(name));
    }

    @GetMapping("/get/id/{id}")
    @Operation(summary = "Получить автора по ID",
            description = "Возвращает автора у которого ID совпадает с введенным, если автор не найден статус 204")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable long id) {
        if (authorService.getAuthorById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping("/getAll")
    @Operation(summary = "Вернуть всех авторов",
            description = "Возвращает список всех авторов, если авторы не найдены статус 204")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        if (authorService.getAllAuthors() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PutMapping("/update/allFields/{id}")
    @Operation(summary = "Обновить автора по ID",
            description = "Обновляет автора ID которого введено, если автор не найден статус 204, при неверном введении статус 400")
    public ResponseEntity<AuthorDTO> updateAuthorById(@PathVariable long id,@RequestBody AuthorDTO authorDTO) {
        if (authorDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (authorService.getAuthorById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(authorService.updateAuthorById(id, authorDTO));
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Обновление введенных полей автора по ID",
            description = "Обновляет поля которые были введены для автора с введенным ID," +
                    " если не находит по id статус 204, при неверном вводе статус 400" )
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable long id, @RequestBody Map<String, Object> updates) {
        if (authorService.getAuthorById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        AuthorDTO updatedAuthor = authorService.updateAuthorFields(id, updates);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить автора по ID",
            description = "Удаляет автора по ID возвращая статус 410, если автор не найден статус 204")
    public ResponseEntity<AuthorDTO> deleteAuthorById(@PathVariable long id) {
        if (authorService.getAuthorById(id) == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        authorService.deleteAuthorById(id);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @GetMapping("/getAllBooks/{authorId}")
    @Operation(summary = "Получить список всех книг автора",
            description = "Возвращает список всех книг которые написал автор, ID которого было введено," +
                    " если автор не найден статус 204")
    public ResponseEntity<List<BookShortDTO>> getAllAuthorsBooks(@PathVariable long authorId) {
        if (authorService.getAuthorById(authorId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(authorService.getAllAuthorsBooks(authorId));
    }
}
