package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.BookDTO;
import kz.halykacademy.bookstore.dto.ModelResponseDTO;
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

import java.util.ArrayList;
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
    public ResponseEntity create(BookDTO bookDTO) {
        // проверка параметров запроса
        checkParameters(bookDTO);

        // конвертирование DTO в Entity
        Book book = bookConvertor.convertToBook(bookDTO);

        List<Author> authors = authorRepository.findAuthorByIdIn(bookDTO.getAuthorIds());

        Optional<Publisher> publisher = publisherRepository.findById(bookDTO.getPublisherId());

        book.setAuthors(authors);
        book.setPublisher(publisher.get());

        // Поиск книги в БД
        Book foundBook = bookRepository.findByTitle(book.getTitle());

        // Проверка существует ли книга
        if (foundBook == null) {
            // если нет, то создаем
            bookRepository.save(book);

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
        // Поиск книги по id
        Optional<Book> bookById = bookRepository.findById(id);

        if (bookById.isEmpty()) {
            // Если не найдена книга
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }

        Book book = bookById.get();

        BookDTO bookDTO = bookConvertor.convertToBookDTO(book);
        List<Long> ids = new ArrayList<>();
        book.getAuthors().forEach(author -> ids.add(author.getId()));

        bookDTO.setAuthorIds(ids);

        return new ResponseEntity(bookDTO, HttpStatus.OK);
    }

    @Override
    public List<BookDTO> readAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookConvertor::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity update(Long id, BookDTO updatedBookDTO) {
        // проверка параметров запроса
        checkParameters(id, updatedBookDTO);

        // Поиск книги по id
        Optional<Book> book = bookRepository.findById(id);

        if (book.isPresent()) {
            // Если найден, обновляем книгу
            bookRepository.save(bookConvertor.convertToBook(updatedBookDTO));

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
    public List<BookDTO> findByTitle(String title) {
        notNull(title, "Title is undefined");

        return bookRepository
                .findByTitleLikeIgnoreCase("%" + title + "%")
                .stream()
                .map(bookConvertor::convertToBookDTO)
                .collect(Collectors.toList());
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

    protected void checkParameters(BookDTO bookDTO) {
        notNull(bookDTO.getTitle(), "Title is undefined");
        notNull(bookDTO.getPrice(), "Price is undefined");
        notNull(bookDTO.getPages(), "Pages is undefined");
        bookDTO.getAuthorIds().forEach(id -> notNull(id, "Author's id is undefined"));
        notNull(bookDTO.getPublisherId(), "Publisher's id is undefined");
        notNull(bookDTO.getPublicationYear(), "Year of publication is undefined");
    }

    protected void checkParameters(Long id, BookDTO bookDTO) {
        notNull(id, "Id is undefined");
        checkParameters(bookDTO);
    }
}
