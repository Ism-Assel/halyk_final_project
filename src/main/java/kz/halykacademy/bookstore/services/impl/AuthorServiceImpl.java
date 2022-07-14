package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.interfaces.AuthorProvider;
import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorProvider authorProvider;

    @Autowired
    public AuthorServiceImpl(AuthorProvider authorProvider) {
        this.authorProvider = authorProvider;
    }

    @Override
    public void create(Author author) {
        authorProvider.create(author);
    }

    @Override
    public Author readById(Long id) {
        return authorProvider.readById(id);
    }

    @Override
    public List<Author> readAll() {
        return authorProvider.getAll();
    }

    @Override
    public void update(Long id) {
        authorProvider.update(id);
    }

    @Override
    public void delete(Long id) {
        authorProvider.delete(id);
    }
}
