package com.example.testapp.controller;

import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.service.GenreService;
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
    public ResponseEntity<GenreDTO> addGenre(@RequestBody GenreDTO genreDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.addGenre(genreDTO));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        List<GenreDTO> genreDTOList = genreService.getAllGenres();
        if (genreDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(genreDTOList);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable long id) {
        GenreDTO genreDTO = genreService.getGenreById(id);
        if (genreDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(genreDTO);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<GenreDTO> updateGenre(@PathVariable long id, @RequestBody GenreDTO genreDTO) {
        GenreDTO dto = genreService.updateGenreById(id, genreDTO);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenreDTO> deleteGenre(@PathVariable long id) {
        GenreDTO genreDTO = genreService.getGenreById(id);
        if (genreDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        genreService.deleteGenreById(id);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<GenreDTO> getGenreByName(@PathVariable String name) {
        GenreDTO genreDTO = genreService.getGenreByName(name);
        if (genreDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(genreDTO);
    }

    @GetMapping("/sort/popularityDesc")
    public ResponseEntity<List<GenreDTO>> getPopularityDesc() {
        List<GenreDTO> genreDTOList = genreService.getMostPopularGenres();
        if (genreDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(genreDTOList);
    }
}
