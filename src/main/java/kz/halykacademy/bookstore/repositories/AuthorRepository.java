package kz.halykacademy.bookstore.repositories;

import kz.halykacademy.bookstore.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = " select * " +
                    "  from author a " +
                    " where lower(a.name) like concat('%', lower(?1), '%') " +
                    "    or lower(a.surname) like concat('%', lower(?2), '%') " +
                    "    or lower(a.lastname) like concat('%', lower(?3), '%') ", nativeQuery = true)
    List<Author> findAuthorByFIOLike(String name, String surname, String lastname);

    Author findAuthorByNameAndSurnameAndLastname(String name, String surname, String lastname);

    List<Author> findAuthorByIdIn(List<Long> id);
}
