package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.PublisherDTO;
import kz.halykacademy.bookstore.services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<PublisherDTO> getAll() {
        return publisherService.readAll();
    }

    @GetMapping("/{id}")
    public PublisherDTO getById(@PathVariable(name = "id") Long id) {
        return publisherService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody PublisherDTO publisherDTO) {
        publisherService.create(publisherDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody PublisherDTO publisherDTO) {
        publisherService.update(publisherDTO.getId(), publisherDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        publisherService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public List<PublisherDTO> getByName(@PathVariable(name = "name") String name) {
        return publisherService.findByNameLike(name);

    }
}
