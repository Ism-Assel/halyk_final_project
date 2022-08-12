package kz.halykacademy.bookstore.models;

import kz.halykacademy.bookstore.dto.author.AuthorResponse;
import kz.halykacademy.bookstore.dto.book.BookResponse;
import kz.halykacademy.bookstore.dto.publisher.PublisherResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private double price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<Author> authors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;

    @Column(name = "title")
    private String title;

    @Column(name = "pages")
    private int pages;

    @Column(name = "publication_year")
    private String publicationYear;

    @ManyToMany
    @JoinTable(
            name = "genre_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres = new ArrayList<>();

    public BookResponse toBookDto() {
        List<AuthorResponse> authorResponses = List.of();

        if (this.authors != null) {
            authorResponses = this.authors.stream().map(Author::toAuthorDto).collect(Collectors.toList());
        }

        PublisherResponse publisherResponse = null;
        if (this.publisher != null) {
            publisherResponse = this.publisher.toPublisherDto();
        }

        return new BookResponse(
                this.id,
                this.title,
                this.price,
                this.pages,
                authorResponses,
                publisherResponse,
                this.publicationYear
        );
    }
}
