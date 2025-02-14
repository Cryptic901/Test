package com.example.testapp.controller;

import com.example.testapp.DTO.AuthorDTO;
import com.example.testapp.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* Контроллер авторов с которым будет взаимодействовать пользователь */

@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/create")
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.addAuthor(authorDTO));
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<AuthorDTO> updateAuthorById(@PathVariable long id,@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.updateAuthorById(id, authorDTO));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAuthorById(@PathVariable long id) {
        authorService.deleteAuthorById(id);
    }

    @GetMapping("/getAllBooks/{authorId}")
    public ResponseEntity<List<Long>> getAllAuthorsBooks(@PathVariable long authorId) {
        return ResponseEntity.ok(authorService.getAllAuthorsBooks(authorId));
    }
}
