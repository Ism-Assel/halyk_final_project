package kz.halykacademy.bookstore.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private double price;

    @ManyToMany(mappedBy = "books")
    private List<Author> authorList;

    @ManyToOne
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;

    @Column(name = "title")
    private String title;

    @Column(name = "pages")
    private int pages;

    @Column(name = "publication_year")
    private LocalDate publicationYear;

    @OneToMany(mappedBy = "book")
    private List<Genre> genres;

    public Book() {
    }

    public Book(
            double price,
            List<Author> authorList,
            Publisher publisher,
            String title,
            int pages,
            LocalDate publicationYear,
            List<Genre> genres
    ) {
        this.price = price;
        this.authorList = authorList;
        this.publisher = publisher;
        this.title = title;
        this.pages = pages;
        this.publicationYear = publicationYear;
        this.genres = genres;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public LocalDate getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(LocalDate publicationYear) {
        this.publicationYear = publicationYear;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
