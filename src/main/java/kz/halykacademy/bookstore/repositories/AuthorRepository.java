package kz.halykacademy.bookstore.repositories;

import kz.halykacademy.bookstore.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByNameOrSurnameOrLastnameLike(String name, String surname, String lastname);
}
