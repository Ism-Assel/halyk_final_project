package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.Genre;
import kz.halykacademy.bookstore.repositories.AuthorRepository;
import kz.halykacademy.bookstore.repositories.BookRepository;
import kz.halykacademy.bookstore.repositories.GenreRepository;
import kz.halykacademy.bookstore.services.GenreService;
import kz.halykacademy.bookstore.utils.convertor.GenreConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;

@Service
@Transactional
public class GenreServiceImpl implements GenreService {
    private final String MESSAGE_NOT_FOUND = "Genre is not found with id = %d";
    private final String MESSAGE_SUCCESS = "success";
    private final String MESSAGE_EXISTED = "This genre is existed";

    private final GenreRepository genreRepository;
    private final GenreConvertor genreConvertor;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository,
                            GenreConvertor genreConvertor,
                            AuthorRepository authorRepository,
                            BookRepository bookRepository
    ) {
        this.genreRepository = genreRepository;
        this.genreConvertor = genreConvertor;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public ResponseEntity create(GenreDTO genreDTO) {
        // проверка параметров запроса
        checkParameters(genreDTO);

        // конвертирование DTO в Entity
        Genre genre = genreConvertor.convertToGenre(genreDTO);

        List<Author> authors = authorRepository.findAuthorByIdIn(genreDTO.getAuthorsId());
        List<Book> books = bookRepository.findBookByIdIn(genreDTO.getBooksId());

        genre.setAuthors(authors);
        genre.setBooks(books);

        // Поиск автора в БД
        Genre foundGenre = genreRepository.findByName(genre.getName());

        // Проверка существует ли жанр
        if (foundGenre == null) {
            // если нет, то создаем
            genreRepository.save(genre);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));

        } else {
            // иначе выводим сообщение пользователю
            throw new ClientBadRequestException(MESSAGE_EXISTED);
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

        Genre genre = genreById.get();

        GenreDTO genreDTO = genreConvertor.convertToGenreDTO(genre);

        List<Long> authorIds = new ArrayList<>();
        List<Long> bookIds = new ArrayList<>();
        genre.getAuthors().forEach(author -> authorIds.add(author.getId()));
        genre.getBooks().forEach(book -> bookIds.add(book.getId()));

        genreDTO.setAuthorsId(authorIds);
        genreDTO.setBooksId(bookIds);

        return new ResponseEntity(genreDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity readAll() {
//        return genreRepository.findAll()
//                .stream()
//                .map(genreConvertor::convertToGenreDTO)
//                .collect(Collectors.toList());
        return null;
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
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));

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
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));
        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    protected void checkParameters(GenreDTO genreDTO) {
        notNull(genreDTO.getName(), "Name is undefined");
        if (genreDTO.getAuthorsId().isEmpty()) {
            throw new ClientBadRequestException("List of authors id is empty");
        }
        if (genreDTO.getBooksId().isEmpty()) {
            throw new ClientBadRequestException("List of books id is empty");
        }
    }

    protected void checkParameters(Long id, GenreDTO genreDTO) {
        notNull(id, "Id is undefined");
        checkParameters(genreDTO);
    }
}
