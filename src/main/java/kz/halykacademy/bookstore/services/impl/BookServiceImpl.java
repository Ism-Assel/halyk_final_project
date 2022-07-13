package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.interfaces.BookProvider;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookProvider bookProvider;

    @Autowired
    public BookServiceImpl(BookProvider bookProvider) {
        this.bookProvider = bookProvider;
    }

    @Override
    public void create(Book book) {
        bookProvider.create(book);
    }

    @Override
    public Book readById(Long id) {
        return bookProvider.readById(id);
    }

    @Override
    public List<Book> readAllBooks() {
        return bookProvider.getAll();
    }

    @Override
    public void update(Long id) {
        bookProvider.update(id);
    }

    @Override
    public void delete(Long id) {
        bookProvider.delete(id);
    }
}
