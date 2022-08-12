package kz.halykacademy.bookstore.dto;

import lombok.Data;

@Deprecated
@Data
public class UserDTO {
    private Long id;
    private String login;
    private String password;
    private String role;
    private Boolean isBlocked;
}
