package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.author.AuthorRequest;
import kz.halykacademy.bookstore.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity getAll() {
        return authorService.readAll();
    }

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
        return authorService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody AuthorRequest request) {
        return authorService.create(request);
    }

    @PutMapping()
    public ResponseEntity put(@RequestBody AuthorRequest request) {
        return authorService.update(request.getId(), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        return authorService.delete(id);
    }

    @GetMapping("/searchByFio")
    public ResponseEntity getByFio(@RequestParam(name = "fio") String fio) {
        return authorService.findByNameOrSurnameOrLastnameLike(fio);
    }

    @GetMapping("/searchByGenres")
    public ResponseEntity getByGenre(@RequestParam(name = "genres") String genres) {
        return authorService.findByGenres(genres);
    }
}
