package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.AuthorDTO;
import kz.halykacademy.bookstore.dto.GenreDTO;
import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.repositories.AuthorRepository;
import kz.halykacademy.bookstore.services.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void create(AuthorDTO authorDTO) {
        authorRepository.save(convertToAuthor(authorDTO));
    }

    @Override
    public AuthorDTO readById(Long id) {
        return convertToAuthorDTO(authorRepository.findById(id).orElse(null));
    }

    @Override
    public List<AuthorDTO> readAll() {
        return authorRepository.findAll().stream()
                .map(this::convertToAuthorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, AuthorDTO updatedAuthorDTO) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            authorRepository.save(convertToAuthor(updatedAuthorDTO));
        }
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public List<AuthorDTO> findByNameOrSurnameOrLastnameLike(String fio) {
        // todo дописать логику
        return authorRepository
                .findByNameOrSurnameOrLastnameLike("", "", "")
                .stream()
                .map(this::convertToAuthorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorDTO> findByGenresIn(List<GenreDTO> genres) {
        // todo исправить
//        return authorRepository.findAuthorByGenresList(genres);
        return null;
    }

    private AuthorDTO convertToAuthorDTO(Author author) {
        return modelMapper.map(author, AuthorDTO.class);
    }

    private Author convertToAuthor(AuthorDTO authorDTO) {
        return modelMapper.map(authorDTO, Author.class);
    }
}
