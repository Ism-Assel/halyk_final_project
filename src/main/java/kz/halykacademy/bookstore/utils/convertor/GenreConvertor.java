package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.genre.GenreResponse;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreConvertor {

    public GenreResponse toGenreDto(Genre genre) {
        List<String> bookResponses = List.of();

        if (genre.getBooks() != null) {
            bookResponses =
                    genre.getBooks()
                            .stream()
                            .map(Book::getTitle)
                            .collect(Collectors.toList());
        }

        return new GenreResponse(
                genre.getId(),
                genre.getName(),
                bookResponses
        );
    }

}