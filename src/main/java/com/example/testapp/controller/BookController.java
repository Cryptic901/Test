package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.model.Books;
import com.example.testapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @DeleteMapping("/private/books/{id}")
    public void deleteBookById(@PathVariable long id) {
        bookService.deleteBookById(id);
    }

    @PostMapping("public/books")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.addBook(bookDTO));
    }

    @PutMapping("public/books/{id}")
    public ResponseEntity<BookDTO> updateBookById(@PathVariable long id,@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBookById(id, bookDTO));
    }

    @GetMapping("public/books/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/public/books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping("public/books/{bookId}/borrow")
    public ResponseEntity<String> borrowBookById(@PathVariable long bookId, @RequestParam long userId) {
        return ResponseEntity.ok(bookService.borrowBookById(bookId, userId));
    }

    @PostMapping("public/books/{bookId}/return")
    public ResponseEntity<String> returnBookById(@PathVariable long bookId, @RequestParam long userId) {
        return ResponseEntity.ok(bookService.returnBookById(bookId, userId));
    }
}
