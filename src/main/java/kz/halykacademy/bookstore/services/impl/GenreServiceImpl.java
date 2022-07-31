package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.models.Genre;
import kz.halykacademy.bookstore.repositories.GenreRepository;
import kz.halykacademy.bookstore.services.GenreService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    public GenreServiceImpl(GenreRepository genreRepository, ModelMapper modelMapper) {
        this.genreRepository = genreRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(GenreDTO genreDTO) {
        genreRepository.save(convertToGenre(genreDTO));
    }

    @Override
    public GenreDTO readById(Long id) {
        return convertToGenreDTO(genreRepository.findById(id).orElse(null));
    }

    @Override
    public List<GenreDTO> readAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(this::convertToGenreDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, GenreDTO updatedGenreDTO) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            genreRepository.save(convertToGenre(updatedGenreDTO));
        }
    }

    @Override
    public void delete(Long id) {
        genreRepository.deleteById(id);
    }

    private GenreDTO convertToGenreDTO(Genre genre) {
        return modelMapper.map(genre, GenreDTO.class);
    }

    private Genre convertToGenre(GenreDTO genreDTO) {
        return modelMapper.map(genreDTO, Genre.class);
    }

}
