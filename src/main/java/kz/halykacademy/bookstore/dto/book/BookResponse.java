package kz.halykacademy.bookstore.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private Double price;
    private Integer pages;
//    private List<Long> authorsId; todo AuthorResponse
//    private Long publisherId; todo PublisherResponse
    private String publicationYear;
//    private List<GenreDTO> genres; todo GenreResponse
}
