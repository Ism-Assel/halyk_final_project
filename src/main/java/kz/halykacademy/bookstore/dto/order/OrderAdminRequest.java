package kz.halykacademy.bookstore.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAdminRequest extends OrderUserRequest{
    private String status;
}
