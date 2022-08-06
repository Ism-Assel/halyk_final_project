package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.UserDTO;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.User;
import kz.halykacademy.bookstore.repositories.UserRepository;
import kz.halykacademy.bookstore.services.UserService;
import kz.halykacademy.bookstore.utils.convertor.UserConvertor;
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
public class UserServiceImpl implements UserService {
    private final String MESSAGE_NOT_FOUND = "User is not found with id = %d";
    private final String MESSAGE_SUCCESS = "success";
    private final String MESSAGE_EXISTED = "This user is existed";

    private final UserRepository userRepository;
    private final UserConvertor userConvertor;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConvertor userConvertor) {
        this.userRepository = userRepository;
        this.userConvertor = userConvertor;
    }

    @Override
    public ResponseEntity create(UserDTO userDTO) {
        // проверка параметров запроса
        checkParameters(userDTO);

        // конвертирование DTO в Entity
        User user = userConvertor.convertToUser(userDTO);

        // Поиск user в БД
        User foundUser = userRepository.findByLogin(user.getLogin());

        // Проверка существует ли user
        if (foundUser == null) {
            userRepository.save(user);

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
        // Поиск user по id
        Optional<User> userById = userRepository.findById(id);

        if (userById.isEmpty()) {
            // Если не найден user
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }

        UserDTO userDTO = userConvertor.convertToUserDTO(userById.get());

        return new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @Override
    public List<UserDTO> readAll() {
        return userRepository.findAll()
                .stream()
                .map(userConvertor::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity update(Long id, UserDTO updatedUserDTO) {
        // проверка параметров запроса
        checkParameters(id, updatedUserDTO);

        // Поиск user по id
        Optional<User> userById = userRepository.findById(id);

        if (userById.isPresent()) {
            // Если найден, обновляем user
            userRepository.save(userConvertor.convertToUser(updatedUserDTO));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));

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

    protected void checkParameters(Long id, UserDTO userDTO) {
        notNull(id, "Id is undefined");
        checkParameters(userDTO);
    }

    protected void checkParameters(UserDTO userDTO) {
        notNull(userDTO.getLogin(), "Login is undefined");
        notNull(userDTO.getPassword(), "Password is undefined");
        notNull(userDTO.getRole(), "Role is undefined");
        notNull(userDTO.getIsBlocked(), "Blocked is empty");
    }
}
