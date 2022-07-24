package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.models.Genre;
import kz.halykacademy.bookstore.repositories.GenreRepository;
import kz.halykacademy.bookstore.services.GenreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public void create(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public Genre readById(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    @Override
    public List<Genre> readAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public void update(Long id, Genre updatedGenre) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            genreRepository.save(updatedGenre);
        }
    }

    @Override
    public void delete(Long id) {
        genreRepository.deleteById(id);
    }

}
