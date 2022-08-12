package kz.halykacademy.bookstore.dto.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorRequest {
    private Long id;
    private String name;
    private String surname;
    private String lastname;
    private LocalDate dateOfBirth;
}