package kz.halykacademy.bookstore.repositories;

import kz.halykacademy.bookstore.models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    List<Publisher> findByNameLikeIgnoreCase(String name);

    Optional<Publisher> findByName(String name);
}
