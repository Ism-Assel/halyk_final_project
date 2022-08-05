package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.AuthorDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthorService extends Service<AuthorDTO>{

    List<AuthorDTO> findByNameOrSurnameOrLastnameLike(String fio);

    ResponseEntity findByGenres(String genres);
}
