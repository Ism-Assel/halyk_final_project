package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.author.AuthorResponse;
import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.models.Genre;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorConvertor {

    public AuthorResponse toAuthorDto(Author author) {
        List<String> responses = List.of();

        if (author.getGenres() != null){
            responses = author.getGenres()
                    .stream()
                    .map(Genre::getName)
                    .collect(Collectors.toList());
        }

        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getSurname(),
                author.getLastname(),
                author.getDateOfBirth(),
                responses
        );
    }
}