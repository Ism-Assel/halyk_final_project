package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.models.Genre;
import kz.halykacademy.bookstore.services.GenreService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;
    private final ModelMapper modelMapper;

    @Autowired
    public GenreController(GenreService genreService, ModelMapper modelMapper) {
        this.genreService = genreService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<GenreDTO> getAll() {
        return genreService
                .readAllGenres()
                .stream()
                .map(this::convertToGenreDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GenreDTO getById(@PathVariable(name = "id") Long id) {
        return convertToGenreDTO(genreService.readById(id));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody GenreDTO genreDTO) {
        genreService.create(convertToGenre(genreDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody GenreDTO genreDTO) {
        genreService.update(genreDTO.getId(), convertToGenre(genreDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        genreService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private GenreDTO convertToGenreDTO(Genre genre) {
        return modelMapper.map(genre, GenreDTO.class);
    }

    private Genre convertToGenre(GenreDTO genreDTO) {
        return modelMapper.map(genreDTO, Genre.class);
    }


}
