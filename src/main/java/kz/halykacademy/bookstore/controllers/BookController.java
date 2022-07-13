package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.BookDTO;
import kz.halykacademy.bookstore.models.Book;
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
    public List<BookDTO> getAll(){
        return bookService
                .readAllBooks()
                .stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookDTO getById(@PathVariable(name = "id") Long id){
       return convertToBookDTO(bookService.readById(id));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody BookDTO bookDTO){
        bookService.create(convertToBook(bookDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@PathVariable(name = "id") Long id){
        bookService.update(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id){
        bookService.delete(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private BookDTO convertToBookDTO(Book book){
        return modelMapper.map(book, BookDTO.class);
    }

    private Book convertToBook(BookDTO bookDTO){
        return modelMapper.map(bookDTO, Book.class);
    }

}
