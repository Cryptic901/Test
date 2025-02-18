package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* Контроллер книг с которым будет взаимодействовать пользователь */

@RestController
@RequestMapping("api/v1/books")
@Tag(name = "Книги", description = "Методы для работы с книгами")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //Отправка запроса и получение ответа на удаление книги
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить книгу по ID", description = "Удаляет книгу по ID")
    public void deleteBookById(@PathVariable long id) {
        bookService.deleteBookById(id);
    }


    //Отправка запроса и получение ответа на добавление книги
    @PostMapping("/create")
    @Operation(summary = "Создать книгу", description = "Создаёт книгу")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.addBook(bookDTO));
    }

    //Отправка запроса и получение ответа на обновление параметра книги
    @PatchMapping("/update/{id}")
    @Operation(summary = "Обновить книгу по ID", description = "Обновляет параметры книги ID которой введено")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable long id,@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBookById(id, bookDTO));
    }

    //Отправка запроса и получение ответа на получение книги по ID
    @GetMapping("/get/id/{id}")
    @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу у которой ID совпадает с введенным")
    public ResponseEntity<BookDTO> getBookById(@PathVariable long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    //Отправка запроса и получение ответа на получение всех книг
    @GetMapping("/getAll")
    @Operation(summary = "Вернуть все книги", description = "Возвращает список всех книг")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/getAll/short")
    @Operation(summary = "Вернуть краткие параметры книги", description = "Возвращает ID и title книги")
    public ResponseEntity<List<BookShortDTO>> getAllBooksShort() { return ResponseEntity.ok(bookService.getAllBooksShort());}

    //Отправка запроса и получение ответа на поиск по isbn
    @GetMapping("/get/isbn/{isbn}")
    @Operation(summary = "Получить книгу по ISBN", description = "Возвращает книгу по введенному международному стандартному книжному номеру")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(bookService.getBookByIsbn(isbn));
    }
}
