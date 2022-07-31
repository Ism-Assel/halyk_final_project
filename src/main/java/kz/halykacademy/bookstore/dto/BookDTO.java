package kz.halykacademy.bookstore.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private Double price;
    private Integer pages;
    private List<AuthorDTO> authors;
    private PublisherDTO publisher;
    private LocalDate publicationYear;
    private List<GenreDTO> genres;
}
