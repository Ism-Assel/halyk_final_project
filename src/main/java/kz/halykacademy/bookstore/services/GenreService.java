package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.GenreDTO;

import java.util.List;

public interface GenreService {
    void create(GenreDTO genreDTO);

    GenreDTO readById(Long id);

    List<GenreDTO> readAllGenres();

    void update(Long id, GenreDTO updatedGenreDTO);

    void delete(Long id);
}
