package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.UserDTO;
import kz.halykacademy.bookstore.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor {
    private final ModelMapper modelMapper;

    @Autowired
    public UserConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
