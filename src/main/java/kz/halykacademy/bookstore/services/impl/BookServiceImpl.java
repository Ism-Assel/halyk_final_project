package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.BookDTO;
import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.repositories.BookRepository;
import kz.halykacademy.bookstore.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(BookDTO bookDTO) {
        bookRepository.save(convertToBook(bookDTO));
    }

    @Override
    public BookDTO readById(Long id) {
        return convertToBookDTO(bookRepository.findById(id).orElse(null));
    }

    @Override
    public List<BookDTO> readAll() {
        return bookRepository.findAll()
                .stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, BookDTO updatedBookDTO) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.save(convertToBook(updatedBookDTO));
        }
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDTO> findByTitle(String title) {
        return bookRepository
                .findByTitleLike(title)
                .stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> findByGenres(List<GenreDTO> genres) {
        // todo ипсравить
//        return bookRepository.findBookByGenresList(genres);
        return null;
    }

    private BookDTO convertToBookDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private Book convertToBook(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }
}
