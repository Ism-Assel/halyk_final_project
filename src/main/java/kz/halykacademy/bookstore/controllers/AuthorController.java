package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.AuthorDTO;
import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.services.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final ModelMapper modelMapper;

    public AuthorController(AuthorService authorService, ModelMapper modelMapper) {
        this.authorService = authorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<AuthorDTO> getAll() {
        return authorService
                .readAll()
                .stream()
                .map(this::convertToAuthorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AuthorDTO getById(@PathVariable(name = "id") Long id) {
        return convertToAuthorDTO(authorService.readById(id));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody AuthorDTO authorDTO) {
        authorService.create(convertToAuthor(authorDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity put(@RequestBody AuthorDTO authorDTO) {
        authorService.update(authorDTO.getId(), convertToAuthor(authorDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        authorService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private AuthorDTO convertToAuthorDTO(Author author) {
        return modelMapper.map(author, AuthorDTO.class);
    }

    private Author convertToAuthor(AuthorDTO authorDTO) {
        return modelMapper.map(authorDTO, Author.class);
    }

    @GetMapping("/{fio}")
    public List<Author> getByFio(@PathVariable(name = "fio") String fio) {
        return authorService.findByNameOrSurnameOrLastnameLike(fio);
    }
}
