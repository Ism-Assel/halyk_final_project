package kz.halykacademy.bookstore.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private Long id;
    private String title;
    private Double price;
    private Integer pages;
    private List<Long> authorsId;
    private Long publisherId;
    private String publicationYear;
}
