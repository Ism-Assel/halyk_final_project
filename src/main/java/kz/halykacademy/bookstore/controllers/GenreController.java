package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity getAll() {
        return genreService.readAll();
    }

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
        return genreService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody GenreDTO genreDTO) {
       return genreService.create(genreDTO);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody GenreDTO genreDTO) {
       return genreService.update(genreDTO.getId(), genreDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        return genreService.delete(id);
    }
}
