package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.BookDTO;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.Genre;
import kz.halykacademy.bookstore.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @Autowired
    public BookController(BookService bookService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<BookDTO> getAll() {
        return bookService
                .readAllBooks()
                .stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookDTO getById(@PathVariable(name = "id") Long id) {
        return convertToBookDTO(bookService.readById(id));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody BookDTO bookDTO) {
        bookService.create(convertToBook(bookDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody BookDTO bookDTO) {
        bookService.update(bookDTO.getId(), convertToBook(bookDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        bookService.delete(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book convertToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    @GetMapping("/{title}")
    public List<Book> getByTitle(@PathVariable(name = "title") String title) {
        return bookService.findByTitle(title);
    }

    @GetMapping("/{genres}")
    public List<Book> getByGenre(@PathVariable(name = "genres") List<Genre> genres) {
        return bookService.findByGenres(genres);
    }

}
