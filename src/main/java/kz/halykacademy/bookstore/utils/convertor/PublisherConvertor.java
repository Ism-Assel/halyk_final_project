package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.publisher.PublisherResponse;
import kz.halykacademy.bookstore.models.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherConvertor {

    public PublisherResponse toPublisherDto(Publisher publisher) {
        return new PublisherResponse(
                publisher.getId(),
                publisher.getName()
        );
    }
}
