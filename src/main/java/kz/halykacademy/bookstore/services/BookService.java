package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.book.BookRequest;
import org.springframework.http.ResponseEntity;

public interface BookService extends Service<BookRequest>{
    ResponseEntity findByTitle(String title);

    ResponseEntity findByGenres(String genres);

}
