package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.publisher.PublisherRequest;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Publisher;
import kz.halykacademy.bookstore.repositories.PublisherRepository;
import kz.halykacademy.bookstore.services.PublisherService;
import kz.halykacademy.bookstore.utils.BlockedUserChecker;
import kz.halykacademy.bookstore.utils.convertor.PublisherConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;
import static kz.halykacademy.bookstore.utils.MessageInfo.*;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherConvertor publisherConvertor;

    @Autowired
    public PublisherServiceImpl(
            PublisherRepository publisherRepository,
            PublisherConvertor publisherConvertor
    ) {
        this.publisherRepository = publisherRepository;
        this.publisherConvertor = publisherConvertor;
    }

    @Override
    public ResponseEntity create(PublisherRequest request) {
        try {
            // проверка параметров запроса
            checkParameters(request);

            // Поиск издателя в БД
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
                        .body(publisherConvertor.toPublisherDto(publisher));

            } else {
                // иначе выводим сообщение пользователю
                throw new ClientBadRequestException(MESSAGE_PUBLISHER_EXISTED);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity readById(Long id) {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // Поиск издателя по id
            Optional<Publisher> foundPublisher = publisherRepository.findById(id);

            if (foundPublisher.isEmpty()) {
                // Если не найден издатель, выводим сообщение
                throw new ResourceNotFoundException(String.format(MESSAGE_PUBLISHER_NOT_FOUND, id));
            }

            return new ResponseEntity(foundPublisher.map(publisherConvertor::toPublisherDto).get(), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity readAll() {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // поиск издателей в БД
            List<Publisher> publishers = publisherRepository.findAll();

            if (publishers.isEmpty()) {
                // Если список пуст, выводим сообщение
                throw new ClientBadRequestException(MESSAGE_LIST_PUBLISHERS);
            }

            return new ResponseEntity(
                    publishers.stream()
                            .map(publisherConvertor::toPublisherDto)
                            .collect(Collectors.toList()), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity update(Long id, PublisherRequest request) {
        try {
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
                        .body(publisherConvertor.toPublisherDto(publisher));

            } else {
                // иначе выводим сообщение пользователю
                throw new ResourceNotFoundException(String.format(MESSAGE_PUBLISHER_NOT_FOUND, id));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
        try {
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
                throw new ResourceNotFoundException(String.format(MESSAGE_PUBLISHER_NOT_FOUND, id));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity findByNameLike(String name) {
        try {
            // проверяем заблокирован ли пользователь
            BlockedUserChecker.checkBlockedUser();

            // Проверка параметра запроса
            notNull(name, "Name is undefined");

            // поиск издателей по имени в БД
            List<Publisher> publishers = publisherRepository.findByNameLikeIgnoreCase(name);

            if (publishers.isEmpty()) {
                // если лист пуст, выводим сообщение пользователю
                throw new ClientBadRequestException(MESSAGE_LIST_PUBLISHERS);
            }

            return new ResponseEntity(
                    publishers.stream()
                            .map(publisherConvertor::toPublisherDto)
                            .collect(Collectors.toList()), HttpStatus.OK);

        } catch (Exception e) {
            throw e;
        }
    }

    protected void checkParameters(Long id, PublisherRequest request) {
        notNull(id, "Id is undefined");
        checkParameters(request);
    }

    protected void checkParameters(PublisherRequest request) {
        notNull(request.getName(), "Name is undefined");
    }
}
