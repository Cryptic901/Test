package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.BookShortDTO;
import com.example.testapp.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Operation(summary = "Удалить книгу по ID", description = "Удаляет книгу по ID отправляя статус 410, если не находит по id статус 204")
    public ResponseEntity<BookDTO> deleteBookById(@PathVariable long id) {
        if (bookService.getBookById(id) == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        bookService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }


    //Отправка запроса и получение ответа на добавление книги
    @PostMapping("/create")
    @Operation(summary = "Создать книгу", description = "Создаёт книгу, при неверном введении отправляет статус 400")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        if (bookDTO == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(bookDTO));
    }

    //Отправка запроса и получение ответа на обновление параметра книги
    @PatchMapping("/update/{id}")
    @Operation(summary = "Обновить книгу по ID", description = "Обновляет параметры книги ID которой введено, если книга не найдена статус 204, при неверном введении статус 400")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable long id, @RequestBody BookDTO bookDTO) {
        if (bookDTO == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (bookService.getBookById(id) == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(bookService.updateBookById(id, bookDTO));
    }

    //Отправка запроса и получение ответа на получение книги по ID
    @GetMapping("/get/id/{id}")
    @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу у которой ID совпадает с введенным, если книга не найдена статус 204")
    public ResponseEntity<BookDTO> getBookById(@PathVariable long id) {
        BookDTO book = bookService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    //Отправка запроса и получение ответа на получение всех книг
    @GetMapping("/getAll")
    @Operation(summary = "Вернуть все книги", description = "Возвращает список всех книг, если книга не найдена статус 204")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/getAll/short")
    @Operation(summary = "Вернуть краткие параметры книги", description = "Возвращает ID и title книги, если книга не найдена статус 204")
    public ResponseEntity<List<BookShortDTO>> getAllBooksShort() {
        List<BookShortDTO> books = bookService.getAllBooksShort();
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    //Отправка запроса и получение ответа на поиск по isbn
    @GetMapping("/get/isbn/{isbn}")
    @Operation(summary = "Получить книгу по ISBN", description = "Возвращает книгу по введенному международному стандартному книжному номеру, если книга не найдена статус 204")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) {
        BookDTO book = bookService.getBookByIsbn(isbn);
        if (book == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(book);
    }

    @GetMapping("/get/title/{title}")
    @Operation(summary = "Получить книгу по названию", description = "Возвращает книгу по введенному названию, если книга не найдена статус 204")
    public ResponseEntity<BookDTO> getBookByTitle(@PathVariable String title) {
        if (bookService.getBookByTitle(title) == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(bookService.getBookByTitle(title));
    }

    @GetMapping("/sort/popularityDesc")
    @Operation(summary = "Сортировка по убыванию популярности", description = "Сортирует книги по количеству раз сколько их брали, если книг нет статус 204")
    public ResponseEntity<List<BookDTO>> getPopularityDesc() {
        List<BookDTO> books = bookService.getMostPopularBooks();
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }
}
