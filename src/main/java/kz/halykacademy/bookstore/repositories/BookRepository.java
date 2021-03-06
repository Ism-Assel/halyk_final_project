package kz.halykacademy.bookstore.repositories;

import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleLike(String title);

    @Query(value = "select b from Book b join b.genres g where g.name in :genres order by g.name ")
    List<Book> findBookByGenresList(@Param("genres") Collection<Genre> genres);
}
