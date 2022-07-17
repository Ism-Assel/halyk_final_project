package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.repositories.BookRepository;
import kz.halykacademy.bookstore.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void create(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Book readById(Long id) {
        return  bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> readAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void update(Long id, Book updatedBook) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()){
            bookRepository.save(updatedBook);
        }
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
