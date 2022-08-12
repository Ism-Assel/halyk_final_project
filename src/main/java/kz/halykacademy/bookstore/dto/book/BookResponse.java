package kz.halykacademy.bookstore.dto.book;

import kz.halykacademy.bookstore.dto.author.AuthorResponse;
import kz.halykacademy.bookstore.dto.publisher.PublisherResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private Double price;
    private Integer pages;
    private List<AuthorResponse> authors;
    private PublisherResponse publisher;
    private String publicationYear;
//    private List<GenreDTO> genres; todo GenreResponse
}
