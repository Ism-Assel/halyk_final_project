package kz.halykacademy.bookstore.repositories;

import kz.halykacademy.bookstore.models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    List<Publisher> findByNameLike(String name);

    Publisher findByName(String name);
}
