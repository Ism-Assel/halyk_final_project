package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.PublisherDTO;
import kz.halykacademy.bookstore.services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
       return publisherService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody PublisherDTO publisherDTO) {
       return publisherService.create(publisherDTO);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody PublisherDTO publisherDTO) {
       return publisherService.update(publisherDTO.getId(), publisherDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        return publisherService.delete(id);
    }

    @GetMapping("/searchByName")
    public List<PublisherDTO> getByName(@RequestParam(name = "name") String name) {
        return publisherService.findByNameLike(name);

    }
}
