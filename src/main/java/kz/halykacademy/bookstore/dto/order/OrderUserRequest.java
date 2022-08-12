package kz.halykacademy.bookstore.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserRequest {
    private Long id;
    private List<Long> booksId;
}