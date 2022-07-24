package kz.halykacademy.bookstore.repositories;

import kz.halykacademy.bookstore.models.Author;
import kz.halykacademy.bookstore.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByNameOrSurnameOrLastnameLike(String name, String surname, String lastname);

    @Query(value = "select a from Author a join a.genres g where g.name in :genres order by g.name ")
    List<Author> findAuthorByGenresList(@Param("genres") Collection<Genre> genres);
}
