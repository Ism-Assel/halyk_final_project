package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.publisher.PublisherRequest;
import org.springframework.http.ResponseEntity;

public interface PublisherService extends Service<PublisherRequest>{

    ResponseEntity findByNameLike(String name);
}
