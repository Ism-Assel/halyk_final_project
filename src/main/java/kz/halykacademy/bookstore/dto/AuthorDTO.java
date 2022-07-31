package kz.halykacademy.bookstore.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AuthorDTO {
    private Long id;
    private String name;
    private String surname;
    private String lastname;
    private LocalDate dateOfBirth;
    private List<BookDTO> books;
    private List<GenreDTO> genres;
}
