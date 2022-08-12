package kz.halykacademy.bookstore.dto.publisher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherRequest {
    private Long id;
    private String name;
}
