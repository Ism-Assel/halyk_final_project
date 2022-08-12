package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.BookDTO;
import kz.halykacademy.bookstore.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity getAll() {
        return bookService.readAll();
    }

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
         return bookService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody BookDTO bookDTO) {
       return bookService.create(bookDTO);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody BookDTO bookDTO) {
        return bookService.update(bookDTO.getId(), bookDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        return bookService.delete(id);
    }

    @GetMapping("/searchByTitle")
    public List<BookDTO> getByTitle(@RequestParam(name = "title") String title) {
        return bookService.findByTitle(title);
    }

    @GetMapping("/searchByGenres")
    public ResponseEntity getByGenre(@RequestParam(name = "genres") String genres) {
        return bookService.findByGenres(genres);
    }
}
