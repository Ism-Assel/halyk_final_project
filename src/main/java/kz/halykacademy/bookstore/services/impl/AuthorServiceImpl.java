package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.AuthorDTO;
import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.repositories.AuthorRepository;
import kz.halykacademy.bookstore.services.AuthorService;
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
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity create(AuthorDTO authorDTO) {
        String message;

        try {
            // проверка параметров запроса
            checkParameters(authorDTO);

            // конвертирование DTO в Entity
            Author author = convertToAuthor(authorDTO);

            // Поиск автора в БД
            Author foundAuthor = authorRepository
                    .findAuthorByNameAndSurnameAndLastname(
                            author.getName(),
                            author.getSurname(),
                            author.getLastname());

            // Проверка существует ли автор
            if (foundAuthor == null) {
                // если нет, то создаем
                authorRepository.save(author);
                message = "success";
                return new ResponseEntity(message, HttpStatus.OK);

            } else {
                // иначе выводим сообщение пользователю
                message = "This author is existed";
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
            // Поиск автора по id
            Optional<Author> authorById = authorRepository.findById(id);

            if (authorById.isEmpty()) {
                // Если не найден автор
                return new ResponseEntity("Author is not found", HttpStatus.BAD_REQUEST);
            }

            AuthorDTO authorDTO = convertToAuthorRequest(authorById.get());
            return new ResponseEntity(authorDTO, HttpStatus.OK);

        } catch (Exception e) {
            String message = "Internal Server Error: " + e.getMessage();
            return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<AuthorDTO> readAll() {
        return authorRepository
                .findAll()
                .stream()
                .map(this::convertToAuthorRequest)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity update(Long id, AuthorDTO updatedAuthorDTO) {
        String message;

        try {
            // проверка параметров запроса
            checkParameters(id, updatedAuthorDTO);

            // Поиск автора по id
            Optional<Author> author = authorRepository.findById(id);
            if (author.isPresent()) {
                // Если найден, обновляем автора
                authorRepository.save(convertToAuthor(updatedAuthorDTO));
                message = "successfully updated";
                return new ResponseEntity(message, HttpStatus.OK);

            } else {
                // иначе выводим сообщение пользователю
                message = "Author is not found";
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

            authorRepository.deleteById(id);

            return new ResponseEntity("success", HttpStatus.OK);

        } catch (Exception e) {
            String message = "Internal Server Error: " + e.getMessage();
            return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<AuthorDTO> findByNameOrSurnameOrLastnameLike(String fio) {
        // todo дописать логику
        return authorRepository
                .findByNameOrSurnameOrLastnameLike("", "", "")
                .stream()
                .map(this::convertToAuthorRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorDTO> findByGenresIn(List<GenreDTO> genres) {
        // todo исправить
//        return authorRepository.findAuthorByGenresList(genres);
        return null;
    }

    private AuthorDTO convertToAuthorRequest(Author author) {
        return modelMapper.map(author, AuthorDTO.class);
    }

    private Author convertToAuthor(AuthorDTO authorDTO) {
        return modelMapper.map(authorDTO, Author.class);
    }

    protected void checkParameters(Long id, AuthorDTO authorDTO) {
        Assert.notNull(id, "Id is undefined");
        checkParameters(authorDTO);
    }

    protected void checkParameters(AuthorDTO authorDTO) {
        notNull(authorDTO.getName(), "Name is undefined");
        notNull(authorDTO.getSurname(), "Surname is undefined");
        Assert.notNull(authorDTO.getDateOfBirth(), "Date of birth is undefined");
    }

    public static void notNull(@Nullable String object, String message) {
        if (object == null || object.equals("")) {
            throw new IllegalArgumentException(message);
        }
    }

}
