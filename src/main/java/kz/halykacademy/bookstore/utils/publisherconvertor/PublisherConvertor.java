package kz.halykacademy.bookstore.utils.publisherconvertor;

import kz.halykacademy.bookstore.dto.PublisherDTO;
import kz.halykacademy.bookstore.models.Publisher;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublisherConvertor {
    private final ModelMapper modelMapper;

    @Autowired
    public PublisherConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PublisherDTO convertToPublisherDTO(Publisher publisher) {
        return modelMapper.map(publisher, PublisherDTO.class);
    }

    public Publisher convertToPublisher(PublisherDTO publisherDTO) {
        return modelMapper.map(publisherDTO, Publisher.class);
    }

}
