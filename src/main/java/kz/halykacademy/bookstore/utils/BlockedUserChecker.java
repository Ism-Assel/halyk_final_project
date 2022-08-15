package kz.halykacademy.bookstore.utils;

import kz.halykacademy.bookstore.errors.ForbiddenException;
import kz.halykacademy.bookstore.models.User;
import kz.halykacademy.bookstore.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BlockedUserChecker {
    public static User checkBlockedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        User user = userDetails.getUser();

        if (user.getRole().equals("ROLE_USER") && user.getIsBlocked()) {
            throw new ForbiddenException("User is blocked");
        }

        return user;
    }
}
