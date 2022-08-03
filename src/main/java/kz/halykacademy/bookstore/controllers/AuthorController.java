package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.AuthorDTO;
import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
        return authorService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody AuthorDTO authorDTO) {
        return authorService.create(authorDTO);
    }

    @PutMapping()
    public ResponseEntity put(@RequestBody AuthorDTO authorDTO) {
        return authorService.update(authorDTO.getId(), authorDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        return authorService.delete(id);
    }

    @GetMapping("/searchByFio")
    public List<AuthorDTO> getByFio(@RequestParam(name = "fio") String fio) {
        return authorService.findByNameOrSurnameOrLastnameLike(fio);
    }

    @GetMapping("/{genres}")
    public List<AuthorDTO> getByGenre(@PathVariable(name = "genres") List<GenreDTO> genres) {
        return authorService.findByGenresIn(genres);
    }
}
