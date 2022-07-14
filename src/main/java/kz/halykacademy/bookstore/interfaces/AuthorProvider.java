package kz.halykacademy.bookstore.interfaces;

import kz.halykacademy.bookstore.models.Author;

public interface AuthorProvider extends ImplPublisher<Author> {
    void create(Author author);

    Author readById(Long id);

    void update(Long id);

    void delete(Long id);
}
