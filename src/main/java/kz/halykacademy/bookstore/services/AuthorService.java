package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.AuthorDTO;
import kz.halykacademy.bookstore.dto.GenreDTO;

import java.util.List;

public interface AuthorService {
    void create(AuthorDTO authorDTO);

    AuthorDTO readById(Long id);

    List<AuthorDTO> readAll();

    void update(Long id, AuthorDTO updatedAuthorDTO);

    void delete(Long id);

    List<AuthorDTO> findByNameOrSurnameOrLastnameLike(String fio);

    List<AuthorDTO> findByGenresIn(List<GenreDTO> genres);
}
