package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.models.Book;

import java.util.List;

public interface BookService {
    void create(Book book);

    Book readById(Long id);

    List<Book> readAllBooks();

    void update(Long id, Book updatedBook);

    void delete(Long id);

}
