package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.user.UserResponse;
import kz.halykacademy.bookstore.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor {

    public UserResponse toUserDto(User user) {

        return new UserResponse(
                user.getId(),
                user.getLogin(),
                user.getRole(),
                user.getIsBlocked()
        );
    }
}