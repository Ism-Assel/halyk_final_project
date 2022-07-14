package kz.halykacademy.bookstore.interfaces.impl;

import kz.halykacademy.bookstore.interfaces.AuthorProvider;
import kz.halykacademy.bookstore.models.Author;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorProviderImpl implements AuthorProvider {
    private static List<Author> authors = new ArrayList<>();

    @Override
    public List<Author> getAll() {
        return authors;
    }

    @Override
    public void create(Author author) {
        // todo пока нет реализации
    }

    @Override
    public Author readById(Long id) {
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
