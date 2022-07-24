package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.models.Genre;

import java.util.List;

public interface AuthorService {
    void create(Author author);

    Author readById(Long id);

    List<Author> readAll();

    void update(Long id, Author updatedAuthor);

    void delete(Long id);

    List<Author> findByNameOrSurnameOrLastnameLike(String fio);

    List<Author> findByGenresIn(List<Genre> genres);
}
