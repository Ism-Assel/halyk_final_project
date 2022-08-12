package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.publisher.PublisherRequest;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Publisher;
import kz.halykacademy.bookstore.repositories.PublisherRepository;
import kz.halykacademy.bookstore.services.PublisherService;
import kz.halykacademy.bookstore.utils.BlockedUserChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {
    private final String MESSAGE_NOT_FOUND = "Publisher is not found with id = %d";
    private final String MESSAGE_SUCCESS = "success";
    private final String MESSAGE_EXISTED = "This publisher is existed";
    private final String MESSAGE_LIST_PUBLISHERS = "List of publishers are empty";

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public ResponseEntity create(PublisherRequest request) {
        // проверка параметров запроса
        checkParameters(request);

        // Поиск автора в БД
        Optional<Publisher> foundPublisher = publisherRepository.findByName(
                request.getName());

        // Проверка существует ли автор
        if (foundPublisher.isEmpty()) {
            // если нет, то создаем
            Publisher publisher = new Publisher(
                    request.getId(),
                    request.getName(),
                    null
            );

            publisher = publisherRepository.save(publisher);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(publisher.toPublisherDto());

        } else {
            // иначе выводим сообщение пользователю
            throw new ClientBadRequestException(MESSAGE_EXISTED);
        }
    }

    @Override
    public ResponseEntity readById(Long id) {
        // проверяем заблокирован ли пользователь
        BlockedUserChecker.checkBlockedUser();

        // Поиск издателя по id
        Optional<Publisher> foundPublisher = publisherRepository.findById(id);

        if (foundPublisher.isEmpty()) {
            // Если не найден издатель
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }

        return new ResponseEntity(foundPublisher.map(Publisher::toPublisherDto).get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity readAll() {
        // проверяем заблокирован ли пользователь
        BlockedUserChecker.checkBlockedUser();

        List<Publisher> publishers = publisherRepository.findAll();
        if (publishers.isEmpty()) {
            throw new ClientBadRequestException(MESSAGE_LIST_PUBLISHERS);
        }

        return new ResponseEntity(
                publishers.stream()
                        .map(Publisher::toPublisherDto)
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(Long id, PublisherRequest request) {
        // проверка параметров запроса
        checkParameters(id, request);

        // Поиск издателя по id
        Optional<Publisher> foundPublisher = publisherRepository.findById(id);

        if (foundPublisher.isPresent()) {
            // Если найден, обновляем издателя
            Publisher publisher = new Publisher(
                    request.getId(),
                    request.getName(),
                    null
            );

            publisher = publisherRepository.save(publisher);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(publisher.toPublisherDto());

        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
        // Проверка параметра id
        notNull(id, "Id is undefined");

        // Поиск издателя по id
        Optional<Publisher> foundPublisher = publisherRepository.findById(id);

        if (foundPublisher.isPresent()) {
            // если нашли, удаляем
            publisherRepository.deleteById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));

        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    @Override
    public ResponseEntity findByNameLike(String name) {
        // проверяем заблокирован ли пользователь
        BlockedUserChecker.checkBlockedUser();

        notNull(name, "Name is undefined");

        List<Publisher> publishers = publisherRepository.findByNameLikeIgnoreCase("%" + name + "%");
        if (publishers.isEmpty()) {
            throw new ClientBadRequestException(MESSAGE_LIST_PUBLISHERS);
        }

        return new ResponseEntity(
                publishers.stream()
                        .map(Publisher::toPublisherDto)
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    protected void checkParameters(Long id, PublisherRequest request) {
        notNull(id, "Id is undefined");
        checkParameters(request);
    }

    protected void checkParameters(PublisherRequest request) {
        notNull(request.getName(), "Name is undefined");
    }
}
