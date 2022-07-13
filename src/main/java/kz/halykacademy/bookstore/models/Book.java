package kz.halykacademy.bookstore.models;

import java.time.LocalDate;
import java.util.List;

public class Book {
    private Long id;
    private double price;
    private List<Author> authorList;
    private Publisher publisher;
    private String title;
    private int pages;
    private LocalDate publicationYear;

    public Book() {
    }

    public Book(
            double price,
            List<Author> authorList,
            Publisher publisher,
            String title,
            int pages,
            LocalDate publicationYear
    ) {
        this.price = price;
        this.authorList = authorList;
        this.publisher = publisher;
        this.title = title;
        this.pages = pages;
        this.publicationYear = publicationYear;
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
}
