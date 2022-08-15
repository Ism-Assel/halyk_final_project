package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.genre.GenreRequest;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.Genre;
import kz.halykacademy.bookstore.repositories.AuthorRepository;
import kz.halykacademy.bookstore.repositories.BookRepository;
import kz.halykacademy.bookstore.repositories.GenreRepository;
import kz.halykacademy.bookstore.services.GenreService;
import kz.halykacademy.bookstore.utils.BlockedUserChecker;
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
import static kz.halykacademy.bookstore.utils.MessageInfo.*;

@Service
@Transactional
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreConvertor genreConvertor;

    @Autowired
    public GenreServiceImpl(
            GenreRepository genreRepository,
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            GenreConvertor genreConvertor
    ) {
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.genreConvertor = genreConvertor;
    }

    @Override
    public ResponseEntity create(GenreRequest request) {
        // проверка параметров запроса
        checkParameters(request);

        // Поиск жанра в БД
        Optional<Genre> foundGenre = genreRepository.findByName(request.getName());

        // Проверка существует ли жанр
        if (foundGenre.isEmpty()) {
            // если нет, то создаем
            List<Author> authors = authorRepository.findAuthorByIdIn(request.getAuthorsId());
            List<Book> books = bookRepository.findBookByIdIn(request.getBooksId());

            Genre genre = new Genre(
                    request.getId(),
                    request.getName(),
                    authors,
                    books
            );

            genre = genreRepository.save(genre);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(genreConvertor.toGenreDto(genre));

        } else {
            // иначе выводим сообщение пользователю
            throw new ClientBadRequestException(MESSAGE_GENRE_EXISTED);
        }
    }

    @Override
    public ResponseEntity readById(Long id) {
        // проверяем заблокирован ли пользователь
        BlockedUserChecker.checkBlockedUser();

        // Поиск жанра по id
        Optional<Genre> foundGenre = genreRepository.findById(id);

        if (foundGenre.isEmpty()) {
            // Если не найден жанр
            throw new ResourceNotFoundException(String.format(MESSAGE_GENRE_NOT_FOUND, id));
        }

        return new ResponseEntity(foundGenre.map(genreConvertor::toGenreDto).get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity readAll() {
        // проверяем заблокирован ли пользователь
        BlockedUserChecker.checkBlockedUser();

        List<Genre> genres = genreRepository.findAll();
        if (genres.isEmpty()) {
            throw new ClientBadRequestException(MESSAGE_LIST_GENRES);
        }

        return new ResponseEntity(
                genres.stream()
                        .map(genreConvertor::toGenreDto)
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(Long id, GenreRequest request) {
        // проверка параметров запроса
        checkParameters(id, request);

        // Поиск жанра по id
        Optional<Genre> foundGenre = genreRepository.findById(id);

        if (foundGenre.isPresent()) {
            // Если найден, обновляем жанр
            List<Author> authors = authorRepository.findAuthorByIdIn(request.getAuthorsId());
            List<Book> books = bookRepository.findBookByIdIn(request.getBooksId());

            Genre genre = new Genre(
                    request.getId(),
                    request.getName(),
                    authors,
                    books
            );

            genre = genreRepository.save(genre);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(genreConvertor.toGenreDto(genre));

        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_GENRE_NOT_FOUND, id));
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
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));
        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_GENRE_NOT_FOUND, id));
        }
    }

    protected void checkParameters(GenreRequest request) {
        notNull(request.getName(), "Name is undefined");
        if (request.getAuthorsId().isEmpty()) {
            throw new ClientBadRequestException("List of authors id is empty");
        }
        if (request.getBooksId().isEmpty()) {
            throw new ClientBadRequestException("List of books id is empty");
        }
    }

    protected void checkParameters(Long id, GenreRequest request) {
        notNull(id, "Id is undefined");
        checkParameters(request);
    }
}