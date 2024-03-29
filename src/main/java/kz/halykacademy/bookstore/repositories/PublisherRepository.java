package kz.halykacademy.bookstore.repositories;

import kz.halykacademy.bookstore.models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Query(value = "" +
            " select p " +
            "   from Publisher p " +
            "  where lower(p.name) like concat('%', lower(:name), '%') ")
    List<Publisher> findByNameLikeIgnoreCase(@Param("name") String name);

    Optional<Publisher> findByName(String name);
}
