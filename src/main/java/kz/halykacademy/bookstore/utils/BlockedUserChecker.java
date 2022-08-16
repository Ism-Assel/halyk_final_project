package kz.halykacademy.bookstore.utils;

import kz.halykacademy.bookstore.errors.ForbiddenException;
import kz.halykacademy.bookstore.models.User;
import kz.halykacademy.bookstore.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BlockedUserChecker {
    public static User checkBlockedUser() {
        // достаем пользователя из Spring security системы
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userDetails.getUser();

        // проверяем роль и статус пользователя
        if (user.getRole().equals("ROLE_USER") && user.getIsBlocked()) {
            // если пользователь заблочен то выводим сообщение
            throw new ForbiddenException("User is blocked");
        }

        return user;
    }
}
