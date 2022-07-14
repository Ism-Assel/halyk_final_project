package kz.halykacademy.bookstore.interfaces;

import kz.halykacademy.bookstore.models.Publisher;

public interface PublisherProvider extends ImplPublisher<Publisher> {

    void create(Publisher publisher);

    Publisher readById(Long id);

    void update(Long id);

    void delete(Long id);
}
