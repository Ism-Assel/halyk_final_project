package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.models.Genre;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenreConvertor {
    private final ModelMapper modelMapper;

    @Autowired
    public GenreConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GenreDTO convertToGenreDTO(Genre genre) {
        return modelMapper.map(genre, GenreDTO.class);
    }

    public Genre convertToGenre(GenreDTO genreDTO) {
        return modelMapper.map(genreDTO, Genre.class);
    }
}
