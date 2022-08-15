package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.book.BookResponse;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookConvertor {

    public BookResponse toBookDto(Book book) {
        List<String> bookResponses = List.of();

        if (book.getAuthors() != null) {
            bookResponses = book.getAuthors()
                    .stream()
                    .map(author -> author.getName() + " " + author.getSurname() + " " + author.getLastname())
                    .collect(Collectors.toList());
        }

        List<String> genreResponses = List.of();

        if (book.getGenres() != null) {
            genreResponses = book.getGenres()
                    .stream()
                    .map(Genre::getName)
                    .collect(Collectors.toList());
        }

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getPrice(),
                book.getPages(),
                bookResponses,
                book.getPublisher().getName(),
                book.getPublicationYear(),
                genreResponses
        );
    }
}