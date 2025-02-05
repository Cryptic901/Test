package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* Контроллер книг с которым будет взаимодействовать пользователь */

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    //Отправка запроса и получение ответа на удаление книги
    @DeleteMapping("/delete/{id}")
    public void deleteBookById(@PathVariable long id) {
        bookService.deleteBookById(id);
    }


    //Отправка запроса и получение ответа на добавление книги
    @PostMapping("/create")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.addBook(bookDTO));
    }

    //Отправка запроса и получение ответа на обновление параметра книги
    @PatchMapping("/update/{id}")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable long id,@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBookById(id, bookDTO));
    }

    //Отправка запроса и получение ответа на получение книги по ID
    @GetMapping("/get/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    //Отправка запроса и получение ответа на получение всех книг
    @GetMapping("/getAll")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

}
