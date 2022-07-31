package kz.halykacademy.bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class PublisherDTO {
    private Long id;
    private String name;
    private List<BookDTO> books;
}
