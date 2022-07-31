package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.AuthorDTO;
import kz.halykacademy.bookstore.dto.GenreDTO;

import java.util.List;

public interface AuthorService extends Service<AuthorDTO>{

    List<AuthorDTO> findByNameOrSurnameOrLastnameLike(String fio);

    List<AuthorDTO> findByGenresIn(List<GenreDTO> genres);
}
