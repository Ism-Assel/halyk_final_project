package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.models.Publisher;

import java.util.List;

public interface PublisherService {
    void create(Publisher publisher);

    Publisher readById(Long id);

    List<Publisher> readAll();

    void update(Long id, Publisher updatedPublisher);

    void delete(Long id);

    List<Publisher> findByNameLike(String name);
}
