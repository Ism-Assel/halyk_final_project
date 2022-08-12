package kz.halykacademy.bookstore.dto.order;

import kz.halykacademy.bookstore.dto.book.BookResponse;
import kz.halykacademy.bookstore.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private List<BookResponse> books;
    private String status;
    private LocalDateTime createdAt;
    private UserResponse user;
}
