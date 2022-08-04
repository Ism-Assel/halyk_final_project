package kz.halykacademy.bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class GenreDTO {
    private Long id;
    private String name;
    private List<Long> authorIds;
    private List<Long> bookIds;
}
