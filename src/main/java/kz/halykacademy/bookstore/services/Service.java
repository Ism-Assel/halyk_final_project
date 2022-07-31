package kz.halykacademy.bookstore.services;

import java.util.List;

public interface Service<T> {
    void create(T dto);

    T readById(Long id);

    List<T> readAll();

    void update(Long id, T updatedDTO);

    void delete(Long id);
}
