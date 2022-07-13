package kz.halykacademy.bookstore.interfaces.impl;

import kz.halykacademy.bookstore.interfaces.BookProvider;
import kz.halykacademy.bookstore.models.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class BookProviderImpl implements BookProvider {
    private static List<Book> books = new ArrayList<>();

    @Override
    public List<Book> getAll() {
        return books;
    }

    @Override
    public void create(Book book) {
        // todo пока нет реализации
    }

    @Override
    public Book readById(Long id) {
        // todo пока нет реализации
        return null;
    }

    @Override
    public void update(Long id) {
        // todo пока нет реализации
    }

    @Override
    public void delete(Long id) {
        // todo пока нет реализации
    }
}
