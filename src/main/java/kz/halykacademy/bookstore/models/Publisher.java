package kz.halykacademy.bookstore.models;

import kz.halykacademy.bookstore.dto.book.BookResponse;
import kz.halykacademy.bookstore.dto.publisher.PublisherResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "publisher")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Publisher {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "publisher")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private List<Book> books = new ArrayList<>();

    public PublisherResponse toPublisherDto() {
        List<BookResponse> bookResponses = List.of();

        if (this.books != null) {
            bookResponses = this.books.stream().map(Book::toBookDto).collect(Collectors.toList());
        }

        return new PublisherResponse(
                this.id,
                this.name
        );
    }
}
