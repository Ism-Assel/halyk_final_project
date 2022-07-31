package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.UserDTO;

import java.util.List;

public interface UserService {
    void create(UserDTO userDTO);

    UserDTO readById(Long id);

    List<UserDTO> readAll();

    void update(Long id, UserDTO updatedUserDTO);

    void delete(Long id);
}
