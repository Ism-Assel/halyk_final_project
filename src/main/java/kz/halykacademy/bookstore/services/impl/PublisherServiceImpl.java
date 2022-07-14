package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.interfaces.PublisherProvider;
import kz.halykacademy.bookstore.models.Publisher;
import kz.halykacademy.bookstore.services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {
    private final PublisherProvider publisherProvider;

    @Autowired
    public PublisherServiceImpl(PublisherProvider publisherProvider) {
        this.publisherProvider = publisherProvider;
    }

    @Override
    public void create(Publisher publisher) {
        publisherProvider.create(publisher);
    }

    @Override
    public Publisher readById(Long id) {
        return publisherProvider.readById(id);
    }

    @Override
    public List<Publisher> readAll() {
        return publisherProvider.getAll();
    }

    @Override
    public void update(Long id) {
        publisherProvider.update(id);
    }

    @Override
    public void delete(Long id) {
        publisherProvider.delete(id);
    }
}
