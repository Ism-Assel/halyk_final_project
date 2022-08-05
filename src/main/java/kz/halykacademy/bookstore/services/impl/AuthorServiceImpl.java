package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.AuthorDTO;
import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.repositories.AuthorRepository;
import kz.halykacademy.bookstore.repositories.BookRepository;
import kz.halykacademy.bookstore.services.AuthorService;
import kz.halykacademy.bookstore.utils.convertor.AuthorConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final String MESSAGE_NOT_FOUND = "Author is not found with id = %d";
    private final String MESSAGE_SUCCESS = "success";
    private final String MESSAGE_EXISTED = "This author is existed";

    private final AuthorRepository authorRepository;
    private final AuthorConvertor authorConvertor;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorServiceImpl(
            AuthorRepository authorRepository,
            AuthorConvertor authorConvertor,
            BookRepository bookRepository
    ) {
        this.authorRepository = authorRepository;
        this.authorConvertor = authorConvertor;
        this.bookRepository = bookRepository;
    }

    @Override
    public ResponseEntity create(AuthorDTO authorDTO) {
        // проверка параметров запроса
        checkParameters(authorDTO);

        // конвертирование DTO в Entity
        Author author = authorConvertor.convertToAuthor(authorDTO);

        // Поиск автора в БД
        Author foundAuthor = authorRepository
                .findAuthorByNameAndSurnameAndLastname(
                        author.getName(),
                        author.getSurname(),
                        author.getLastname());

        // Проверка существует ли автор
        if (foundAuthor == null) {
            // если нет, то создаем
            authorRepository.save(author);

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
        // Поиск автора по id
        Optional<Author> authorById = authorRepository.findById(id);

        if (authorById.isEmpty()) {
            // Если не найден автор
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }

        AuthorDTO authorDTO = authorConvertor.convertToAuthorDTO(authorById.get());

        return new ResponseEntity(authorDTO, HttpStatus.OK);
    }

    @Override
    public List<AuthorDTO> readAll() {
        return authorRepository
                .findAll()
                .stream()
                .map(authorConvertor::convertToAuthorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity update(Long id, AuthorDTO updatedAuthorDTO) {

        // проверка параметров запроса
        checkParameters(id, updatedAuthorDTO);

        // Поиск автора по id
        Optional<Author> author = authorRepository.findById(id);

        if (author.isPresent()) {
            // Если найден, обновляем автора
            authorRepository.save(authorConvertor.convertToAuthor(updatedAuthorDTO));

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

        // Поиск автора по Id
        Optional<Author> foundAuthor = authorRepository.findById(id);

        if (foundAuthor.isPresent()) {
            // если нашли, удаляем
            authorRepository.deleteById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(String.format(MESSAGE_SUCCESS)));
        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    @Override
    public List<AuthorDTO> findByNameOrSurnameOrLastnameLike(String fio) {
        notNull(fio, "FIO is empty");

        String fioLike = "%" + fio + "%";

        List<Author> author =
                authorRepository.findAuthorByFIOLike(fioLike, fioLike, fioLike);

        return author.stream()
                .map(authorConvertor::convertToAuthorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity findByGenres(String genres) {
        String[] genresAsArray = genres.split(",");
        genresAsArray =
                Arrays.stream(genresAsArray)
                        .map(String::trim)
                        .toArray(String[]::new);

        Set<Author> authors = new HashSet<>();

        List<Book> books = bookRepository.findBookByGenres(genresAsArray);
        books.forEach(book -> authors.addAll(book.getAuthors()));

        List<AuthorDTO> authorsDTO =
                authors.stream()
                        .map(authorConvertor::convertToAuthorDTO)
                        .collect(Collectors.toList());

        return new ResponseEntity(authorsDTO, HttpStatus.OK);

    }

    protected void checkParameters(Long id, AuthorDTO authorDTO) {
        notNull(id, "Id is undefined");
        checkParameters(authorDTO);
    }

    protected void checkParameters(AuthorDTO authorDTO) {
        notNull(authorDTO.getName(), "Name is undefined");
        notNull(authorDTO.getSurname(), "Surname is undefined");
        notNull(authorDTO.getDateOfBirth(), "Date of birth is undefined");
    }
}