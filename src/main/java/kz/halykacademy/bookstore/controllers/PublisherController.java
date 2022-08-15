package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.publisher.PublisherRequest;
import kz.halykacademy.bookstore.services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity getAll() {
        return publisherService.readAll();
    }

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
       return publisherService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody PublisherRequest request) {
       return publisherService.create(request);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody PublisherRequest request) {
       return publisherService.update(request.getId(), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        return publisherService.delete(id);
    }

    @GetMapping("/searchByName")
    public ResponseEntity getByName(@RequestParam(name = "name") String name) {
        return publisherService.findByNameLike(name);
    }
}
