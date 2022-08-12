package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.BookDTO;
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

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final String MESSAGE_NOT_FOUND = "Book is not found with id = %d";
    private final String MESSAGE_SUCCESS = "success";
    private final String MESSAGE_EXISTED = "This book is existed";
    private final String MESSAGE_LIST_BOOKS = "List of books are empty";

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
        // проверка параметров запроса
        checkParameters(request);

        // Поиск книги в БД
        Optional<Book> foundBook = bookRepository.findByTitle(request.getTitle());

        // Проверка существует ли книга
        if (foundBook.isEmpty()) {
            // если нет, то создаем
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

            book = bookRepository.save(book);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(book.toBookDto());

        } else {
            // иначе выводим сообщение пользователю
            throw new ClientBadRequestException(MESSAGE_EXISTED);
        }
    }

    @Override
    public ResponseEntity readById(Long id) {
        // Поиск книги по id
        Optional<Book> foundBook = bookRepository.findById(id);

        if (foundBook.isEmpty()) {
            // Если не найдена книга
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }

//        Book book = foundBook.get();
//
//        BookDTO bookDTO = bookConvertor.convertToBookDTO(book);
//        List<Long> ids = new ArrayList<>();
//        book.getAuthors().forEach(author -> ids.add(author.getId()));
//
//        bookDTO.setAuthorsId(ids);

        return new ResponseEntity(foundBook.map(Book::toBookDto).get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity readAll() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new ClientBadRequestException(MESSAGE_LIST_BOOKS);
        }

        return new ResponseEntity(
                books.stream()
                        .map(Book::toBookDto)
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(Long id, BookRequest request) {
        // проверка параметров запроса
        checkParameters(id, request);

        // Поиск книги по id
        Optional<Book> foundBook = bookRepository.findById(id);

        if (foundBook.isPresent()) {
            // Если найден, обновляем книгу
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

            book = bookRepository.save(book);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(book.toBookDto());

        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
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
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    @Override
    public ResponseEntity findByTitle(String title) {
        notNull(title, "Title is undefined");

        List<Book> books = bookRepository
                .findByTitleLikeIgnoreCase("%" + title + "%");

        if (books.isEmpty()) {
            throw new ClientBadRequestException(MESSAGE_LIST_BOOKS);
        }

        return new ResponseEntity(
                books.stream()
                        .map(Book::toBookDto)
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity findByGenres(String genres) {
        String[] genresAsArray = genres.split(",");
        genresAsArray =
                Arrays.stream(genresAsArray)
                        .map(String::trim)
                        .toArray(String[]::new);

        List<BookDTO> books = bookRepository.findBookByGenres(genresAsArray)
                .stream()
                .map(bookConvertor::convertToBookDTO)
                .collect(Collectors.toList());

        return new ResponseEntity(books, HttpStatus.OK);
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
}
