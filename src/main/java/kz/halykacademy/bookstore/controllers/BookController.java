package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.book.BookRequest;
import kz.halykacademy.bookstore.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity getAll() {
        return bookService.readAll();
    }

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
         return bookService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody BookRequest request) {
       return bookService.create(request);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody BookRequest request) {
        return bookService.update(request.getId(), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        return bookService.delete(id);
    }

    @GetMapping("/searchByTitle")
    public ResponseEntity getByTitle(@RequestParam(name = "title") String title) {
        return bookService.findByTitle(title);
    }

    @GetMapping("/searchByGenres")
    public ResponseEntity getByGenre(@RequestParam(name = "genres") String genres) {
        return bookService.findByGenres(genres);
    }
}
