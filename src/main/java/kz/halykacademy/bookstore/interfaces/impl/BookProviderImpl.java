package kz.halykacademy.bookstore.interfaces.impl;

import kz.halykacademy.bookstore.interfaces.BookProvider;
import kz.halykacademy.bookstore.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookProviderImpl implements BookProvider {
    private static List<Book> books = new ArrayList<>();

    @Override
    public List<Book> getAll() {
        return books;
    }
}
