package com.example.testapp.controller;

import com.example.testapp.DTO.BookDTO;
import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.impl.GenreServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/* Контроллер жанров с которым будет взаимодействовать пользователь */

@RestController
@RequestMapping("api/v1/genres")
@Tag(name = "Жанры",
        description = "Методы для работы с жанрами")
public class GenreController {

    private final GenreServiceImpl genreService;

    @Autowired
    public GenreController(GenreServiceImpl genreService) {
        this.genreService = genreService;
    }

    @PostMapping("/create")
    @Operation(summary = "Добавление жанра",
            description = "Добавляет жанр, при неверном введении отправляет статус 400")
    public ResponseEntity<GenreDTO> addGenre(@RequestBody GenreDTO genreDTO) {
        GenreDTO genre = genreService.addGenre(genreDTO);
        if (genreDTO == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(genre);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Получение всех жанров",
            description = "Возвращает список всех жанров, если список пустой статус 204")
    public ResponseEntity<List<GenreDTO>> getAllGenre() {
        List<GenreDTO> genreDTOList = genreService.getAllGenre();
        if (genreDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(genreDTOList);
    }

    @GetMapping("/get/id/{id}")
    @Operation(summary = "Получение жанра по ID",
            description = "Возвращает жанр с введенным названием, если жанр не найден статус 204")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable long id) {
        GenreDTO genreDTO = genreService.getGenreById(id);
        if (genreDTO == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(genreDTO);
    }

    @PutMapping("/update/allFields/{id}")
    @Operation(summary = "Обновление жанра по ID",
            description = "Обновляет жанр, если не находит по id статус 204, при неверном введении статус 400")
    public ResponseEntity<GenreDTO> updateGenreById(@PathVariable long id, @RequestBody GenreDTO genreDTO) {
        GenreDTO genre = genreService.getGenreById(id);
        if (genreDTO == null) {
            return ResponseEntity.badRequest().body(null);
        }
        GenreDTO dto = genreService.updateGenreById(id, genreDTO);
        if (genre == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/update/{id}")
    @Operation(summary = "Обновление введенных полей жанра по ID",
            description = "Обновляет поля которые были введены для пользователя с введенным ID," +
                    " если не находит по id статус 204, при неверном вводе статус 400")
    public ResponseEntity<GenreDTO> updateGenre(@PathVariable long id, @RequestBody Map<String, Object> updates) {
        GenreDTO genre = genreService.getGenreById(id);
        if (genre == null) {
            return ResponseEntity.noContent().build();
        }
        if (updates == null) {
            return ResponseEntity.badRequest().body(null);
        }
        GenreDTO genreDTO = genreService.updateGenreFields(id, updates);
        return ResponseEntity.ok(genreDTO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление жанра по ID",
            description = "Удаляет жанр по введенному ID отправляя статус 410, если не находит по id статус 204")
    public ResponseEntity<GenreDTO> deleteGenre(@PathVariable long id) {
        String response = genreService.deleteGenreById(id);
        if (response.equals("Genre not found")) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @GetMapping("/get/name/{name}")
    @Operation(summary = "Поиск жанра по названию",
            description = "Находит жанр по введенному названию, если не находит то статус 204")
    public ResponseEntity<GenreDTO> getGenreByName(@PathVariable String name) {
        GenreDTO genreDTO = genreService.getGenreByName(name);
        if (genreDTO == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(genreDTO);
    }

    @GetMapping("/sort/popularityDesc")
    @Operation(summary = "Сортирует жанры по популярности",
            description = "Сортирует жанры по убыванию количества занятых книг за всё время," +
                    " если жанров нет статус 204")
    public ResponseEntity<List<GenreDTO>> getPopularityDesc() {
        List<GenreDTO> genreDTOList = genreService.getMostPopularGenre();
        if (genreDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(genreDTOList);
    }

    @GetMapping("/getAllBooks")
    @Operation(summary = "Получить все книги в жанре по айди",
            description = "Возвращает список книг у которых genreId совпадает с введенным в параметр запроса")
    public ResponseEntity<List<BookDTO>> getAllBooksInThatGenre(@RequestParam long genreId) {
        List<BookDTO> books = genreService.getAllBooksInGenreById(genreId);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }
}
