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

/* Контроллер авторов с которым будет взаимодействовать пользователь */

@RestController
@RequestMapping("api/v1/authors")
@Tag(name = "Авторы", description = "Методы для работы с авторами")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/create")
    @Operation(summary = "Создать автора", description = "Создаёт автора")
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody AuthorDTO authorDTO) {
        if (authorDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(authorService.addAuthor(authorDTO));
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<AuthorDTO> getAuthorByName(@PathVariable String name) {
        if (authorService.getAuthorByName(name) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(authorService.getAuthorByName(name));
    }

    @GetMapping("/get/id/{id}")
    @Operation(summary = "Получить автора по ID", description = "Возвращает автора у которого ID совпадает с введенным")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable long id) {
        if (authorService.getAuthorById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping("/getAll")
    @Operation(summary = "Вернуть всех авторов", description = "Возвращает список всех авторов")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        if (authorService.getAllAuthors() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Обновить автора по ID", description = "Обновляет параметры автора ID которого введено")
    public ResponseEntity<AuthorDTO> updateAuthorById(@PathVariable long id,@RequestBody AuthorDTO authorDTO) {
        if (authorDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(authorService.updateAuthorById(id, authorDTO));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить автора по ID", description = "Удаляет автора по ID")
    public ResponseEntity<AuthorDTO> deleteAuthorById(@PathVariable long id) {
        if (authorService.getAuthorById(id) == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        authorService.deleteAuthorById(id);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @GetMapping("/getAllBooks/{authorId}")
    @Operation(summary = "Получить список всех книг автора", description = "Возвращает список всех книг которые написал автор, ID которого было введено")
    public ResponseEntity<List<BookShortDTO>> getAllAuthorsBooks(@PathVariable long authorId) {
        if (authorService.getAuthorById(authorId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(authorService.getAllAuthorsBooks(authorId));
    }
}
