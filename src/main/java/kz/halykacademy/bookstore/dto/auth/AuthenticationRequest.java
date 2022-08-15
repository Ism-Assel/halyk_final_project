package kz.halykacademy.bookstore.dto.auth;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String login;
    private String password;
}
