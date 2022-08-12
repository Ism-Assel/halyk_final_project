package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.genre.GenreRequest;
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
    public ResponseEntity post(@RequestBody GenreRequest request) {
       return genreService.create(request);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody GenreRequest request) {
       return genreService.update(request.getId(), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        return genreService.delete(id);
    }
}
