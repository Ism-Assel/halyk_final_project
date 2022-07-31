package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.PublisherDTO;

import java.util.List;

public interface PublisherService extends Service<PublisherDTO>{

    List<PublisherDTO> findByNameLike(String name);
}
