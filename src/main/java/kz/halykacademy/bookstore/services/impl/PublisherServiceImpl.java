package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.PublisherDTO;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Publisher;
import kz.halykacademy.bookstore.repositories.PublisherRepository;
import kz.halykacademy.bookstore.services.PublisherService;
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

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {
    private final String MESSAGE_NOT_FOUND = "Publisher is not found with id = %d";
    private final String MESSAGE_SUCCESS = "success";
    private final String MESSAGE_EXISTED = "This publisher is existed";

    private final PublisherRepository publisherRepository;
    private final PublisherConvertor publisherConvertor;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository, PublisherConvertor publisherConvertor) {
        this.publisherRepository = publisherRepository;
        this.publisherConvertor = publisherConvertor;
    }

    @Override
    public ResponseEntity create(PublisherDTO publisherDTO) {
        // проверка параметров запроса
        checkParameters(publisherDTO);

        // конвертирование DTO в Entity
        Publisher publisher = publisherConvertor.convertToPublisher(publisherDTO);

        // Поиск автора в БД
        Publisher foundPublisher = publisherRepository.findByName(
                publisher.getName());

        // Проверка существует ли автор
        if (foundPublisher == null) {
            // если нет, то создаем
            publisherRepository.save(publisher);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));

        } else {
            // иначе выводим сообщение пользователю
            throw new ClientBadRequestException(MESSAGE_EXISTED);
        }
    }

    @Override
    public ResponseEntity readById(Long id) {
        // Поиск издателя по id
        Optional<Publisher> publisherById = publisherRepository.findById(id);

        if (publisherById.isEmpty()) {
            // Если не найден издатель
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }

        PublisherDTO publisherDTO = publisherConvertor.convertToPublisherDTO(publisherById.get());

        return new ResponseEntity(publisherDTO, HttpStatus.OK);
    }

    @Override
    public List<PublisherDTO> readAll() {
        return publisherRepository.findAll()
                .stream()
                .map(publisherConvertor::convertToPublisherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity update(Long id, PublisherDTO updatedPublisherDTO) {
        // проверка параметров запроса
        checkParameters(id, updatedPublisherDTO);

        // Поиск издателя по id
        Optional<Publisher> publisher = publisherRepository.findById(id);

        if (publisher.isPresent()) {
            // Если найден, обновляем издателя
            publisherRepository.save(publisherConvertor.convertToPublisher(updatedPublisherDTO));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));

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
    public List<PublisherDTO> findByNameLike(String name) {
        notNull(name, "Name is undefined");

        return publisherRepository
                .findByNameLikeIgnoreCase("%" + name + "%")
                .stream()
                .map(publisherConvertor::convertToPublisherDTO)
                .collect(Collectors.toList());
    }

    protected void checkParameters(Long id, PublisherDTO publisherDTO) {
        notNull(id, "Id is undefined");
        checkParameters(publisherDTO);
    }

    protected void checkParameters(PublisherDTO publisherDTO) {
        notNull(publisherDTO.getName(), "Name is undefined");
    }
}
