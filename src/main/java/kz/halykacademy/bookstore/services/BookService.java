package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.BookDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService extends Service<BookDTO>{
    List<BookDTO> findByTitle(String title);

    ResponseEntity findByGenres(String genres);

}
