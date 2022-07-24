package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.models.Publisher;
import kz.halykacademy.bookstore.repositories.PublisherRepository;
import kz.halykacademy.bookstore.services.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void create(Publisher publisher) {
        publisherRepository.save(publisher);
    }

    @Override
    public Publisher readById(Long id) {
        return publisherRepository.findById(id).orElse(null);
    }

    @Override
    public List<Publisher> readAll() {
        return publisherRepository.findAll();
    }

    @Override
    public void update(Long id, Publisher updatedPublisher) {
        Optional<Publisher> publisher = publisherRepository.findById(id);
        if (publisher.isPresent()) {
            publisherRepository.save(updatedPublisher);
        }
    }

    @Override
    public void delete(Long id) {
        publisherRepository.deleteById(id);
    }

    @Override
    public List<Publisher> findByNameLike(String name) {
        return publisherRepository.findByNameLike(name);
    }
}
