package kz.halykacademy.bookstore.repositories;

import kz.halykacademy.bookstore.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "select b from Book b where lower(b.title) like concat('%', lower(:title), '%')")
    List<Book> findByTitleLikeIgnoreCase(@Param("title") String title);

    @Query(value = "select b from Book b join b.genres g where g.name in :genres order by g.name ")
    List<Book> findBookByGenres(@Param("genres") String[] genres);

    Optional<Book> findByTitle(String title);

    List<Book> findBookByIdIn(List<Long> id);
}
