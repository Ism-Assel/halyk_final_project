package kz.halykacademy.bookstore.interfaces.impl;

import kz.halykacademy.bookstore.interfaces.PublisherProvider;
import kz.halykacademy.bookstore.models.Publisher;

import java.util.ArrayList;
import java.util.List;

public class PublisherProviderImpl implements PublisherProvider {
    private static List<Publisher> publishers = new ArrayList<>();

    @Override
    public List<Publisher> getAll() {
        return publishers;
    }
}
