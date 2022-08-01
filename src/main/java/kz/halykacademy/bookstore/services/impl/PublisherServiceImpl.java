package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.PublisherDTO;
import kz.halykacademy.bookstore.models.Publisher;
import kz.halykacademy.bookstore.repositories.PublisherRepository;
import kz.halykacademy.bookstore.services.PublisherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
        String message;

        try {
            // проверка параметров запроса
            checkParameters(publisherDTO);

            // конвертирование DTO в Entity
            Publisher publisher = convertToPublisher(publisherDTO);

            // Поиск автора в БД
            Publisher foundPublisher = publisherRepository.findByName(
                    publisher.getName());

            // Проверка существует ли автор
            if (foundPublisher == null) {
                // если нет, то создаем
                publisherRepository.save(publisher);
                message = "success";
                return new ResponseEntity(message, HttpStatus.OK);
            } else {
                // иначе выводим сообщение пользователю
                message = "This publisher is existed";
                return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            message = "Internal Server Error: " + e.getMessage();
            return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity readById(Long id) {
        try {
            // Поиск издателя по id
            Optional<Publisher> publisherById = publisherRepository.findById(id);

            if (publisherById.isEmpty()) {
                // Если не найден издатель
                return new ResponseEntity("Publisher is not found", HttpStatus.BAD_REQUEST);
            }
            PublisherDTO publisherDTO = convertToPublisherDTO(publisherById.get());
            return new ResponseEntity(publisherDTO, HttpStatus.OK);

        } catch (Exception e) {
            String message = "Internal Server Error: " + e.getMessage();
            return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        String message;

        try {
            // проверка параметров запроса
            checkParameters(id, updatedPublisherDTO);

            // Поиск издателя по id
            Optional<Publisher> publisher = publisherRepository.findById(id);
            if (publisher.isPresent()) {
                // Если найден, обновляем издателя
                publisherRepository.save(convertToPublisher(updatedPublisherDTO));
                message = "successfully updated";
                return new ResponseEntity(message, HttpStatus.OK);

            } else {
                // иначе выводим сообщение пользователю
                message = "Publisher is not found";
                return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            message = "Internal Server Error: " + e.getMessage();
            return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
        try {
            Assert.notNull(id, "Id is undefined");

            publisherRepository.deleteById(id);

            return new ResponseEntity("success", HttpStatus.OK);

        } catch (Exception e) {
            String message = "Internal Server Error: " + e.getMessage();
            return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    protected void checkParameters(Long id, PublisherDTO publisherDTO) {
        Assert.notNull(id, "Id is undefined");
        checkParameters(publisherDTO);
    }

    protected void checkParameters(PublisherDTO publisherDTO) {
        notNull(publisherDTO.getName(), "Name is undefined");
    }

    public static void notNull(@Nullable String object, String message) {
        if (object == null || object.equals("")) {
            throw new IllegalArgumentException(message);
        }
    }
}
