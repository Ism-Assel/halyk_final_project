package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.models.Genre;

import java.util.List;

public interface GenreService {
    void create(Genre genre);

    Genre readById(Long id);

    List<Genre> readAllGenres();

    void update(Long id, Genre updatedGenre);

    void delete(Long id);
}
