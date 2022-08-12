package kz.halykacademy.bookstore.dto;

import lombok.Data;

import java.util.List;

@Deprecated
@Data
public class BookDTO {
    private Long id;
    private String title;
    private Double price;
    private Integer pages;
    private List<Long> authorsId;
    private Long publisherId;
    private String publicationYear;
    private List<GenreDTO> genres;
}
