package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.AuthorDTO;
import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDTO> getAll() {
        return authorService.readAll();
    }

    @GetMapping("/{id}")
    public AuthorDTO getById(@PathVariable(name = "id") Long id) {
        return authorService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody AuthorDTO authorDTO) {
        authorService.create(authorDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity put(@RequestBody AuthorDTO authorDTO) {
        authorService.update(authorDTO.getId(), authorDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        authorService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{fio}")
    public List<AuthorDTO> getByFio(@PathVariable(name = "fio") String fio) {
        return authorService.findByNameOrSurnameOrLastnameLike(fio);
    }

    @GetMapping("/{genres}")
    public List<AuthorDTO> getByGenre(@PathVariable(name = "genres") List<GenreDTO> genres) {
        return authorService.findByGenresIn(genres);
    }
}
