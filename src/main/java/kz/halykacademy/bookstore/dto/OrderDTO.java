package kz.halykacademy.bookstore.dto;

import lombok.Data;

import java.util.List;

@Deprecated
@Data
public class OrderDTO {
    private Long id;
    private List<Long> booksId;
    private String status;
    private Long userId;
}
