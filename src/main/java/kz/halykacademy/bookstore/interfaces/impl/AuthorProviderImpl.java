package kz.halykacademy.bookstore.interfaces.impl;

import kz.halykacademy.bookstore.interfaces.AuthorProvider;
import kz.halykacademy.bookstore.models.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorProviderImpl implements AuthorProvider {
    private static List<Author> authors = new ArrayList<>();

    @Override
    public List<Author> getAll() {
        return authors;
    }
}
