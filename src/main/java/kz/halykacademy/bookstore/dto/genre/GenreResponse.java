package kz.halykacademy.bookstore.dto.genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreResponse {
    private Long id;
    private String name;
    private List<String> books;
}
