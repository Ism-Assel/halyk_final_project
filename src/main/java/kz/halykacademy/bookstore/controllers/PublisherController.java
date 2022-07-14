package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.PublisherDTO;
import kz.halykacademy.bookstore.models.Publisher;
import kz.halykacademy.bookstore.services.PublisherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService publisherService;
    private final ModelMapper modelMapper;

    @Autowired
    public PublisherController(PublisherService publisherService, ModelMapper modelMapper) {
        this.publisherService = publisherService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<PublisherDTO> getAll() {
        return publisherService
                .readAll()
                .stream()
                .map(this::convertToPublisherDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PublisherDTO getById(@PathVariable(name = "id") Long id) {
        return convertToPublisherDTO(publisherService.readById(id));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody PublisherDTO publisherDTO) {
        publisherService.create(convertToPublisher(publisherDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity put(@PathVariable(name = "id") Long id) {
        publisherService.update(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        publisherService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private PublisherDTO convertToPublisherDTO(Publisher publisher) {
        return modelMapper.map(publisher, PublisherDTO.class);
    }

    private Publisher convertToPublisher(PublisherDTO publisherDTO) {
        return modelMapper.map(publisherDTO, Publisher.class);
    }
}
