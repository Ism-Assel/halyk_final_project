package kz.halykacademy.bookstore.dto.book;

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
    private List<String> authors;
    private String publisher;
    private String publicationYear;
    private List<String> genres;
}
