package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.BookDTO;
import kz.halykacademy.bookstore.dto.GenreDTO;

import java.util.List;

public interface BookService extends Service<BookDTO>{
    List<BookDTO> findByTitle(String title);

    List<BookDTO> findByGenres(List<GenreDTO> genres);

}
