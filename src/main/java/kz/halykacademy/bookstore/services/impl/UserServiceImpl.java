package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.UserDTO;
import kz.halykacademy.bookstore.models.User;
import kz.halykacademy.bookstore.repositories.UserRepository;
import kz.halykacademy.bookstore.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(UserDTO userDTO) {
        userRepository.save(convertToUser(userDTO));
    }

    @Override
    public UserDTO readById(Long id) {
        return convertToUserDTO(userRepository.findById(id).orElse(null));
    }

    @Override
    public List<UserDTO> readAll() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, UserDTO updatedUserDTO) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.save(convertToUser(updatedUserDTO));
        }
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
