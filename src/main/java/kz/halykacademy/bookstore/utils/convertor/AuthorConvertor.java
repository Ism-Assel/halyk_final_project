package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.AuthorDTO;
import kz.halykacademy.bookstore.models.Author;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Deprecated
@Component
public class AuthorConvertor {

    private final ModelMapper modelMapper;

    @Autowired
    public AuthorConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public AuthorDTO convertToAuthorDTO(Author author) {
        return modelMapper.map(author, AuthorDTO.class);
    }

    public Author convertToAuthor(AuthorDTO authorDTO) {
        return modelMapper.map(authorDTO, Author.class);
    }
}
