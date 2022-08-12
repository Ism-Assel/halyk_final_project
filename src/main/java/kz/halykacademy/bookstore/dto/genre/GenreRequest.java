package kz.halykacademy.bookstore.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreRequest {
    private Long id;
    private String name;
    private List<Long> authorsId;
    private List<Long> booksId;
}
