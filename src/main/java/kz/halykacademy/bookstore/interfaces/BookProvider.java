package kz.halykacademy.bookstore.interfaces;

import kz.halykacademy.bookstore.models.Book;

public interface BookProvider extends ImplPublisher<Book> {
    void create(Book book);

    Book readById(Long id);

    void update(Long id);

    void delete(Long id);


}
