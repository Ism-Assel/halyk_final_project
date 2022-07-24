package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.models.Genre;
import kz.halykacademy.bookstore.repositories.AuthorRepository;
import kz.halykacademy.bookstore.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void create(Author author) {
        authorRepository.save(author);
    }

    @Override
    public Author readById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Author> readAll() {
        return authorRepository.findAll();
    }

    @Override
    public void update(Long id, Author updatedAuthor) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            authorRepository.save(updatedAuthor);
        }
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public List<Author> findByNameOrSurnameOrLastnameLike(String fio) {

        // todo дописать логику
        return authorRepository.findByNameOrSurnameOrLastnameLike("", "", "");
    }

    @Override
    public List<Author> findByGenresIn(List<Genre> genres) {
        return authorRepository.findAuthorByGenresList(genres);
    }
}
