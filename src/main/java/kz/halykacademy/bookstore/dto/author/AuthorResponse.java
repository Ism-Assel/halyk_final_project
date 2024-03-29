package kz.halykacademy.bookstore.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {
    private Long id;
    private String name;
    private String surname;
    private String lastname;
    private LocalDate dateOfBirth;
    private List<String> genres;
}