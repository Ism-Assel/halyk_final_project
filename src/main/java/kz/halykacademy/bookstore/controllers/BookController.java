package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.BookDTO;
import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDTO> getAll() {
        return bookService.readAll();
    }

    @GetMapping("/{id}")
    public BookDTO getById(@PathVariable(name = "id") Long id) {
        return bookService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody BookDTO bookDTO) {
        bookService.create(bookDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody BookDTO bookDTO) {
        bookService.update(bookDTO.getId(), bookDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        bookService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{title}")
    public List<BookDTO> getByTitle(@PathVariable(name = "title") String title) {
        return bookService.findByTitle(title);
    }

    @GetMapping("/{genres}")
    public List<BookDTO> getByGenre(@PathVariable(name = "genres") List<GenreDTO> genres) {
        return bookService.findByGenres(genres);
    }
}
