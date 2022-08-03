package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Genre;
import kz.halykacademy.bookstore.repositories.GenreRepository;
import kz.halykacademy.bookstore.services.GenreService;
import kz.halykacademy.bookstore.utils.convertor.GenreConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;

@Service
@Transactional
public class GenreServiceImpl implements GenreService {
    private final String MESSAGE_NOT_FOUND = "Genre is not found with id = %d";
    private final String MESSAGE_SUCCESS = "success";
    private final String MESSAGE_EXISTED = "This genre is existed";

    private final GenreRepository genreRepository;
    private final GenreConvertor genreConvertor;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, GenreConvertor genreConvertor) {
        this.genreRepository = genreRepository;
        this.genreConvertor = genreConvertor;
    }

    @Override
    public ResponseEntity create(GenreDTO genreDTO) {
        // проверка параметров запроса
        checkParameters(genreDTO);

        // конвертирование DTO в Entity
        Genre genre = genreConvertor.convertToGenre(genreDTO);

        // Поиск автора в БД
        Genre foundGenre = genreRepository.findByName(genre.getName());

        // Проверка существует ли жанр
        if (foundGenre == null) {
            // если нет, то создаем
            genreRepository.save(genre);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(String.format(MESSAGE_SUCCESS)));

        } else {
            // иначе выводим сообщение пользователю
            throw new ClientBadRequestException(String.format(MESSAGE_EXISTED));
        }
    }

    @Override
    public ResponseEntity readById(Long id) {
        // Поиск жанра по id
        Optional<Genre> genreById = genreRepository.findById(id);

        if (genreById.isEmpty()) {
            // Если не найден жанр
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }

        GenreDTO genreDTO = genreConvertor.convertToGenreDTO(genreById.get());

        return new ResponseEntity(genreDTO, HttpStatus.OK);
    }

    @Override
    public List<GenreDTO> readAll() {
        return genreRepository.findAll()
                .stream()
                .map(genreConvertor::convertToGenreDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity update(Long id, GenreDTO updatedGenreDTO) {
        // проверка параметров запроса
        checkParameters(id, updatedGenreDTO);

        // Поиск жанра по id
        Optional<Genre> genre = genreRepository.findById(id);

        if (genre.isPresent()) {
            // Если найден, обновляем жанр
            genreRepository.save(genreConvertor.convertToGenre(updatedGenreDTO));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(String.format(MESSAGE_SUCCESS)));

        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
        // Проверка параметра id
        notNull(id, "Id is undefined");

        // Поиск жанра по Id
        Optional<Genre> foundGenre = genreRepository.findById(id);

        if (foundGenre.isPresent()) {
            // если нашли, удаляем
            genreRepository.deleteById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(String.format(MESSAGE_SUCCESS)));
        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    protected void checkParameters(GenreDTO genreDTO) {
        notNull(genreDTO.getName(), "Name is undefined");
    }

    protected void checkParameters(Long id, GenreDTO genreDTO) {
        notNull(id, "Id is undefined");
        checkParameters(genreDTO);
    }
}
