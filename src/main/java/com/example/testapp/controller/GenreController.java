package com.example.testapp.controller;

import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* Контроллер жанров с которым будет взаимодействовать пользователь */

@RestController
@RequestMapping("api/v1/genres")
@Tag(name = "Жанры", description = "Методы для работы с жанрами")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping("/create")
    @Operation(summary = "Добавление жанра", description = "Добавляет жанр, при неверном введении отправляет статус 400")
    public ResponseEntity<GenreDTO> addGenre(@RequestBody GenreDTO genreDTO) {
        if (genreDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.addGenre(genreDTO));
    }

    @GetMapping("/getAll")
    @Operation(summary = "Получение всех жанров", description = "Возвращает список всех жанров, если список пустой статус 204")
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        List<GenreDTO> genreDTOList = genreService.getAllGenres();
        if (genreDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(genreDTOList);
    }

    @GetMapping("/get/id/{id}")
    @Operation(summary = "Получение жанра по ID", description = "Возвращает жанр с введенным названием, если жанр не найден статус 204")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable long id) {
        GenreDTO genreDTO = genreService.getGenreById(id);
        if (genreDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(genreDTO);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Обновление жанра по ID", description = "Обновляет жанр, если не находит по id статус 204, при неверном введении статус 400")
    public ResponseEntity<GenreDTO> updateGenre(@PathVariable long id, @RequestBody GenreDTO genreDTO) {
        if (genreDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        GenreDTO dto = genreService.updateGenreById(id, genreDTO);
        if (genreService.getGenreById(id) == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удаление жанра по ID", description = "Удаляет жанр по введенному ID отправляя статус 410, если не находит по id статус 204")
    public ResponseEntity<GenreDTO> deleteGenre(@PathVariable long id) {
        GenreDTO genreDTO = genreService.getGenreById(id);
        if (genreDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        genreService.deleteGenreById(id);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @GetMapping("/get/name/{name}")
    @Operation(summary = "Поиск жанра по названию", description = "Находит жанр по введенному названию, если не находит то статус 204")
    public ResponseEntity<GenreDTO> getGenreByName(@PathVariable String name) {
        GenreDTO genreDTO = genreService.getGenreByName(name);
        if (genreDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(genreDTO);
    }

    @GetMapping("/sort/popularityDesc")
    @Operation(summary = "Сортирует жанры по популярности", description = "Сортирует жанры по убыванию количества занятых книг за всё время, если жанров нет статус 204")
    public ResponseEntity<List<GenreDTO>> getPopularityDesc() {
        List<GenreDTO> genreDTOList = genreService.getMostPopularGenres();
        if (genreDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(genreDTOList);
    }
}
