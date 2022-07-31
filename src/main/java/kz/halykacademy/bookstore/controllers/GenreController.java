package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<GenreDTO> getAll() {
        return genreService.readAllGenres();
    }

    @GetMapping("/{id}")
    public GenreDTO getById(@PathVariable(name = "id") Long id) {
        return genreService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody GenreDTO genreDTO) {
        genreService.create(genreDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody GenreDTO genreDTO) {
        genreService.update(genreDTO.getId(), genreDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        genreService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
