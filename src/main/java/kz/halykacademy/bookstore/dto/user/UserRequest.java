package kz.halykacademy.bookstore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Long id;
    private String login;
    private String password;
    private String role;
    private Boolean isBlocked;
}
