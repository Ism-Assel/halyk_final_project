package kz.halykacademy.bookstore.interfaces.impl;

import kz.halykacademy.bookstore.interfaces.PublisherProvider;
import kz.halykacademy.bookstore.models.Publisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PublisherProviderImpl implements PublisherProvider {
    private static List<Publisher> publishers = new ArrayList<>();

    @Override
    public List<Publisher> getAll() {
        return publishers;
    }

    @Override
    public void create(Publisher publisher) {
        // todo пока нет реализации
    }

    @Override
    public Publisher readById(Long id) {
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
