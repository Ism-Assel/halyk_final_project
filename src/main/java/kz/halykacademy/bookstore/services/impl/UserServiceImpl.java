package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.user.UserRequest;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.User;
import kz.halykacademy.bookstore.repositories.UserRepository;
import kz.halykacademy.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final String MESSAGE_NOT_FOUND = "User is not found with id = %d";
    private final String MESSAGE_SUCCESS = "success";
    private final String MESSAGE_EXISTED = "This user is existed";
    private final String MESSAGE_LIST_USERS = "List of users are empty";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity create(UserRequest request) {
        // проверка параметров запроса
        checkParameters(request);

        // Поиск user в БД
        Optional<User> foundUser = userRepository.findByLogin(request.getLogin());

        // Проверка существует ли user
        if (foundUser.isEmpty()) {
            User user = new User(
                    request.getId(),
                    request.getLogin(),
                    passwordEncoder.encode(request.getPassword()),
                    request.getRole().toUpperCase(),
                    request.getIsBlocked(),
                    null
            );

            user = userRepository.save(user);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user.toUserDto());

        } else {
            // иначе выводим сообщение пользователю
            throw new ClientBadRequestException(MESSAGE_EXISTED);
        }
    }

    @Override
    public ResponseEntity readById(Long id) {
        // Поиск user по id
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isEmpty()) {
            // Если не найден user
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }

        return new ResponseEntity(foundUser.map(User::toUserDto).get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity readAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ClientBadRequestException(MESSAGE_LIST_USERS);
        }

        return new ResponseEntity(
                users.stream()
                        .map(User::toUserDto)
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(Long id, UserRequest request) {
        // проверка параметров запроса
        checkParameters(id, request);

        // Поиск user по id
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isPresent()) {
            // Если найден, обновляем user
            User user = new User(
                    request.getId(),
                    request.getLogin(),
                    passwordEncoder.encode(request.getPassword()),
                    request.getRole().toUpperCase(),
                    request.getIsBlocked(),
                    null
            );

            user = userRepository.save(user);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user.toUserDto());

        } else {
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
        // Проверка параметра id
        notNull(id, "Id is undefined");

        // Поиск user по id
        Optional<User> foundUser = userRepository.findById(id);

        if (foundUser.isPresent()) {
            // если нашли, удаляем
            userRepository.deleteById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));

        } else {
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    protected void checkParameters(Long id, UserRequest request) {
        notNull(id, "Id is undefined");
        checkParameters(request);
    }

    protected void checkParameters(UserRequest request) {
        notNull(request.getLogin(), "Login is undefined");
        notNull(request.getPassword(), "Password is undefined");
        notNull(request.getRole(), "Role is undefined");
        notNull(request.getIsBlocked(), "Blocked is empty");
    }
}
