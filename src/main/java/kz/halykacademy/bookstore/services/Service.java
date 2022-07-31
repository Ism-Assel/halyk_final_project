package kz.halykacademy.bookstore.services;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface Service<T> {
    ResponseEntity create(T dto);

    ResponseEntity readById(Long id);

    List<T> readAll();

    ResponseEntity update(Long id, T updatedDTO);

    ResponseEntity delete(Long id);
}
