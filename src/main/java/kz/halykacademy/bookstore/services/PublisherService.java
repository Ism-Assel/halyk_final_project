package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.PublisherDTO;

import java.util.List;

public interface PublisherService {
    void create(PublisherDTO publisherDTO);

    PublisherDTO readById(Long id);

    List<PublisherDTO> readAll();

    void update(Long id, PublisherDTO updatedPublisherDTO);

    void delete(Long id);

    List<PublisherDTO> findByNameLike(String name);
}
