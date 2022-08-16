package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.book.BookRequest;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.Publisher;
import kz.halykacademy.bookstore.repositories.AuthorRepository;
import kz.halykacademy.bookstore.repositories.BookRepository;
import kz.halykacademy.bookstore.repositories.PublisherRepository;
import kz.halykacademy.bookstore.services.BookService;
import kz.halykacademy.bookstore.utils.BlockedUserChecker;
import kz.halykacademy.bookstore.utils.convertor.BookConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;
import static kz.halykacademy.bookstore.utils.MessageInfo.*;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final BookConvertor bookConvertor;

    @Autowired
    public BookServiceImpl(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            PublisherRepository publisherRepository,
            BookConvertor bookConvertor
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.bookConvertor = bookConvertor;
    }

    @Override
    public ResponseEntity create(BookRequest request) {
        try {
            // проверка параметров запроса
            checkParameters(request);

            // Поиск книги в БД
            Optional<Book> foundBook = bookRepository.findByTitle(request.getTitle());

            // Проверка существует ли книга
            if (foundBook.isEmpty()) {
                // если нет, то создаем и сохраняем
                Book book = prepareBook(request);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(bookConvertor.toBookDto(book));

            } else {
                // иначе выводим сообщение пользователю
                throw new ClientBadRequestException(MESSAGE_BOOK_EXISTED);
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

            // Поиск книги по id
            Optional<Book> foundBook = bookRepository.findById(id);

            if (foundBook.isEmpty()) {
                // Если не найдена книга, выводим сообщение
                throw new ResourceNotFoundException(String.format(MESSAGE_BOOK_NOT_FOUND, id));
            }

            return new ResponseEntity(foundBook.map(bookConvertor::toBookDto).get(), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity readAll() {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // поиск книг в БД
            List<Book> books = bookRepository.findAll();

            if (books.isEmpty()) {
                // если пуст список, выводим сообщение
                throw new ClientBadRequestException(MESSAGE_LIST_BOOKS);
            }

            return new ResponseEntity(
                    books.stream()
                            .map(bookConvertor::toBookDto)
                            .collect(Collectors.toList()), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity update(Long id, BookRequest request) {
        try {
            // проверка параметров запроса
            checkParameters(id, request);

            // Поиск книги по id
            Optional<Book> foundBook = bookRepository.findById(id);

            if (foundBook.isPresent()) {
                // Если найден, обновляем книгу
                Book book = prepareBook(request);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(bookConvertor.toBookDto(book));

            } else {
                // иначе выводим сообщение пользователю
                throw new ResourceNotFoundException(String.format(MESSAGE_BOOK_NOT_FOUND, id));
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

            // Поиск книги по Id
            Optional<Book> book = bookRepository.findById(id);

            if (book.isPresent()) {
                // если нашли, удаляем
                bookRepository.deleteById(id);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ModelResponseDTO(MESSAGE_SUCCESS));
            } else {
                // иначе выводим сообщение пользователю
                throw new ResourceNotFoundException(String.format(MESSAGE_BOOK_NOT_FOUND, id));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity findByTitle(String title) {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // проверка параметров запроса
            notNull(title, "Title is undefined");

            // поиск книг по названию в БД
            List<Book> books = bookRepository
                    .findByTitleLikeIgnoreCase(title);

            if (books.isEmpty()) {
                // если список пуст, выводим сообщение
                throw new ClientBadRequestException(MESSAGE_LIST_BOOKS);
            }

            return new ResponseEntity(
                    books.stream()
                            .map(bookConvertor::toBookDto)
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

            // создаем массив жанров, разделенный запятой
            String[] genresAsArray = genres.split(",");

            // удаление пробелов в каждом элементе массива
            genresAsArray =
                    Arrays.stream(genresAsArray)
                            .map(String::trim)
                            .toArray(String[]::new);

            // поиск книг по жанрам в БД
            List<Book> books = bookRepository.findBookByGenres(genresAsArray);

            return new ResponseEntity(
                    books.stream()
                            .map(bookConvertor::toBookDto)
                            .collect(Collectors.toSet()),
                    HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    protected void checkParameters(BookRequest request) {
        notNull(request.getTitle(), "Title is undefined");
        notNull(request.getPrice(), "Price is undefined");
        notNull(request.getPages(), "Pages is undefined");
        if (request.getAuthorsId().isEmpty()) {
            throw new ClientBadRequestException("List of authors id is empty");
        }
        notNull(request.getPublisherId(), "Publisher's id is undefined");
        notNull(request.getPublicationYear(), "Year of publication is undefined");
    }

    protected void checkParameters(Long id, BookRequest request) {
        notNull(id, "Id is undefined");
        checkParameters(request);
    }

    protected Book prepareBook(BookRequest request){
        List<Author> authors = authorRepository.findAuthorByIdIn(request.getAuthorsId());
        Optional<Publisher> publisher = publisherRepository.findById(request.getPublisherId());

        Book book = new Book(
                request.getId(),
                request.getPrice(),
                authors,
                publisher.get(),
                request.getTitle(),
                request.getPages(),
                request.getPublicationYear(),
                null
        );

        return bookRepository.save(book);
    }
}
