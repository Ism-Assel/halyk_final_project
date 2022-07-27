package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.models.User;

import java.util.List;

public interface UserService {
    void create(User user);

    User readById(Long id);

    List<User> readAll();

    void update(Long id, User updatedUser);

    void delete(Long id);
}
