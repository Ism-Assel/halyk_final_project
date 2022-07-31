package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.BookDTO;
import kz.halykacademy.bookstore.dto.GenreDTO;

import java.util.List;

public interface BookService {
    void create(BookDTO bookDTO);

    BookDTO readById(Long id);

    List<BookDTO> readAllBooks();

    void update(Long id, BookDTO updatedBookDTO);

    void delete(Long id);

    List<BookDTO> findByTitle(String title);

    List<BookDTO> findByGenres(List<GenreDTO> genres);

}
