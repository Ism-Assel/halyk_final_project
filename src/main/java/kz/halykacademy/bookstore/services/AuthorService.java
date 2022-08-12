package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.author.AuthorRequest;
import org.springframework.http.ResponseEntity;

public interface AuthorService extends Service<AuthorRequest>{

    ResponseEntity findByNameOrSurnameOrLastnameLike(String fio);

    ResponseEntity findByGenres(String genres);
}
