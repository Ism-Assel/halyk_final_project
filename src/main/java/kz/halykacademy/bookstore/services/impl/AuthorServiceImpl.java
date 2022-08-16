package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.author.AuthorRequest;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.repositories.AuthorRepository;
import kz.halykacademy.bookstore.repositories.BookRepository;
import kz.halykacademy.bookstore.services.AuthorService;
import kz.halykacademy.bookstore.utils.BlockedUserChecker;
import kz.halykacademy.bookstore.utils.convertor.AuthorConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;
import static kz.halykacademy.bookstore.utils.MessageInfo.*;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorConvertor authorConvertor;

    @Autowired
    public AuthorServiceImpl(
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            AuthorConvertor authorConvertor
    ) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.authorConvertor = authorConvertor;
    }

    @Override
    public ResponseEntity create(AuthorRequest request) {
        try {
            // проверка параметров запроса
            checkParameters(request);

            // Поиск автора в БД
            Author foundAuthor = authorRepository
                    .findAuthorByNameAndSurnameAndLastname(
                            request.getName(),
                            request.getSurname(),
                            request.getLastname());

            // Проверка существует ли автор
            if (foundAuthor == null) {
                // если нет, то создаем и сохраняем
                Author author = new Author(
                        request.getId(),
                        request.getName(),
                        request.getSurname(),
                        request.getLastname(),
                        request.getDateOfBirth(),
                        null,
                        null
                );

                author = authorRepository.save(author);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(authorConvertor.toAuthorDto(author));

            } else {
                // иначе выводим сообщение пользователю
                throw new ClientBadRequestException(MESSAGE_AUTHOR_EXISTED);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity readById(Long id) {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // Поиск автора по id
            Optional<Author> foundAuthor = authorRepository.findById(id);

            if (foundAuthor.isEmpty()) {
                // Если не найден автор выводим сообщение
                throw new ResourceNotFoundException(String.format(MESSAGE_AUTHOR_NOT_FOUND, id));
            }

            return new ResponseEntity(
                    foundAuthor.map(authorConvertor::toAuthorDto).get(),
                    HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity readAll() {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // поиск авторов в БД
            List<Author> authors = authorRepository.findAll();

            if (authors.isEmpty()) {
                // Если список пуст, выводим сообщение
                throw new ClientBadRequestException(MESSAGE_LIST_AUTHORS);
            }

            return new ResponseEntity(
                    authors.stream()
                            .map(authorConvertor::toAuthorDto)
                            .collect(Collectors.toList()), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity update(Long id, AuthorRequest request) {
        try {
            // проверка параметров запроса
            checkParameters(id, request);

            // Поиск автора по id
            Optional<Author> foundAuthor = authorRepository.findById(id);

            if (foundAuthor.isPresent()) {
                // Если найден, обновляем и сохраняем автора
                Author author = new Author(
                        request.getId(),
                        request.getName(),
                        request.getSurname(),
                        request.getLastname(),
                        request.getDateOfBirth(),
                        null,
                        null
                );

                author = authorRepository.save(author);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(authorConvertor.toAuthorDto(author));

            } else {
                // иначе выводим сообщение пользователю
                throw new ResourceNotFoundException(String.format(MESSAGE_AUTHOR_NOT_FOUND, id));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
        try {
            // Проверка параметра id
            notNull(id, "Id is undefined");

            // Поиск автора по Id
            Optional<Author> foundAuthor = authorRepository.findById(id);

            if (foundAuthor.isPresent()) {
                // если нашли, удаляем
                authorRepository.deleteById(id);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ModelResponseDTO(MESSAGE_SUCCESS));

            } else {
                // иначе выводим сообщение пользователю
                throw new ResourceNotFoundException(String.format(MESSAGE_AUTHOR_NOT_FOUND, id));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity findByNameOrSurnameOrLastnameLike(String fio) {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // Проверка параметра fio
            notNull(fio, "FIO is empty");

            // поиск авторов в БД
            List<Author> authors =
                    authorRepository.findAuthorByFIOLike(fio, fio, fio);

            if (authors.isEmpty()) {
                // если пуст список выводим сообщение
                throw new ClientBadRequestException(MESSAGE_LIST_AUTHORS);
            }

            return new ResponseEntity(
                    authors.stream()
                            .map(authorConvertor::toAuthorDto)
                            .collect(Collectors.toList()), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity findByGenres(String genres) {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // создаем массив строк, разделенный запятой
            String[] genresAsArray = genres.split(",");

            // удаление пробелов в каждом элементе массива
            genresAsArray =
                    Arrays.stream(genresAsArray)
                            .map(String::trim)
                            .toArray(String[]::new);

            Set<Author> authors = new HashSet<>();

            // поиск книг по жанрам в БД
            List<Book> books = bookRepository.findBookByGenres(genresAsArray);

            // у каждой книги получаем автора и добавляем в HashSet
            books.forEach(book -> authors.addAll(book.getAuthors()));

            return new ResponseEntity(
                    authors.stream()
                            .map(authorConvertor::toAuthorDto)
                            .collect(Collectors.toList()),
                    HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    protected void checkParameters(Long id, AuthorRequest request) {
        notNull(id, "Id is undefined");
        checkParameters(request);
    }

    protected void checkParameters(AuthorRequest request) {
        notNull(request.getName(), "Name is undefined");
        notNull(request.getSurname(), "Surname is undefined");
        notNull(request.getDateOfBirth(), "Date of birth is undefined");
    }
}