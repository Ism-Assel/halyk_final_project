package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.PublisherDTO;
import kz.halykacademy.bookstore.models.Publisher;
import kz.halykacademy.bookstore.repositories.PublisherRepository;
import kz.halykacademy.bookstore.services.PublisherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository, ModelMapper modelMapper) {
        this.publisherRepository = publisherRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity create(PublisherDTO publisherDTO) {
        publisherRepository.save(convertToPublisher(publisherDTO));
        return null;
    }

    @Override
    public ResponseEntity readById(Long id) {
        convertToPublisherDTO(publisherRepository.findById(id).orElse(null));
        return null;
    }

    @Override
    public List<PublisherDTO> readAll() {
        return publisherRepository.findAll()
                .stream()
                .map(this::convertToPublisherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity update(Long id, PublisherDTO updatedPublisherDTO) {
        Optional<Publisher> publisher = publisherRepository.findById(id);
        if (publisher.isPresent()) {
            publisherRepository.save(convertToPublisher(updatedPublisherDTO));
        }
        return null;
    }

    @Override
    public ResponseEntity delete(Long id) {
        publisherRepository.deleteById(id);
        return null;
    }

    @Override
    public List<PublisherDTO> findByNameLike(String name) {
        return publisherRepository
                .findByNameLike(name)
                .stream()
                .map(this::convertToPublisherDTO)
                .collect(Collectors.toList());
    }

    private PublisherDTO convertToPublisherDTO(Publisher publisher) {
        return modelMapper.map(publisher, PublisherDTO.class);
    }

    private Publisher convertToPublisher(PublisherDTO publisherDTO) {
        return modelMapper.map(publisherDTO, Publisher.class);
    }

}
