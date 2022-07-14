package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.models.Author;

import java.util.List;

public interface AuthorService {
    void create(Author author);

    Author readById(Long id);

    List<Author> readAll();

    void update(Long id);

    void delete(Long id);
}
