package kz.halykacademy.bookstore.services;

import org.springframework.http.ResponseEntity;

public interface Service<T> {
    ResponseEntity create(T dto);

    ResponseEntity readById(Long id);

    ResponseEntity readAll();

    ResponseEntity update(Long id, T updatedDTO);

    ResponseEntity delete(Long id);
}
