package com.example.testapp.controller;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.impl.AuthorServiceImpl;
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

    private final AuthorServiceImpl authorService;

    @Autowired
    public AuthorController(AuthorServiceImpl authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/create")
    @Operation(summary = "Создать автора",
            description = "Создаёт автора, при неверном введении отправляет статус 400")
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody AuthorDTO authorDTO) {
        AuthorDTO result = authorService.addAuthor(authorDTO);
        if (authorDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/get/name/{name}")
    @Operation(summary = "Получить автора по имени",
            description = "Возвращает автора с именем совпадающим с введенным, если автор не найден статус 204")
    public ResponseEntity<AuthorDTO> getAuthorByName(@PathVariable String name) {
        AuthorDTO author = authorService.getAuthorByName(name);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @GetMapping("/get/id/{id}")
    @Operation(summary = "Получить автора по ID",
            description = "Возвращает автора у которого ID совпадает с введенным, если автор не найден статус 204")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Вернуть всех авторов",
            description = "Возвращает список всех авторов, если авторы не найдены статус 204")
    public ResponseEntity<List<AuthorDTO>> getAllAuthor() {
        List<AuthorDTO> authors = authorService.getAllAuthor();
        return authors == null ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(authors);
    }

    @PutMapping("/update/allFields/{id}")
    @Operation(summary = "Обновить автора по ID",
            description = "Обновляет автора ID которого введено, если автор не найден статус 204, при неверном введении статус 400")
    public ResponseEntity<AuthorDTO> updateAuthorById(@PathVariable long id,@RequestBody AuthorDTO authorDTO) {
        AuthorDTO result = authorService.updateAuthorById(id, authorDTO);
        AuthorDTO author = authorService.getAuthorById(id);
        if (authorDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (author == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Обновление введенных полей автора по ID",
            description = "Обновляет поля которые были введены для автора с введенным ID," +
                    " если не находит по id статус 204, при неверном вводе статус 400" )
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable long id, @RequestBody Map<String, Object> updates) {
        AuthorDTO author = authorService.getAuthorById(id);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        AuthorDTO updatedAuthor = authorService.updateAuthorFields(id, updates);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить автора по ID",
            description = "Удаляет автора по ID возвращая статус 410, если автор не найден статус 204")
    public ResponseEntity<AuthorDTO> deleteAuthorById(@PathVariable long id) {
        String response = authorService.deleteAuthorById(id);
        if (response.equals("Author not found")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @GetMapping("/getAllBook/{authorId}")
    @Operation(summary = "Получить список всех книг автора",
            description = "Возвращает список всех книг которые написал автор, ID которого было введено," +
                    " если автор не найден статус 204")
    public ResponseEntity<List<BookShortDTO>> getAllAuthorBook(@PathVariable long authorId) {
        AuthorDTO author = authorService.getAuthorById(authorId);
        List<BookShortDTO> result = authorService.getAllAuthorBook(authorId);
        if (author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
