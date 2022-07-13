package kz.halykacademy.bookstore.models;

import java.time.LocalDate;
import java.util.List;

public class Author {
    private Long id;
    private String name;
    private String surname;
    private String lastname;
    private LocalDate dateOfBirth;
    private List<Book> books;

    public Author() {
    }

    public Author(
            String name,
            String surname,
            String lastname,
            LocalDate dateOfBirth,
            List<Book> books
    ) {
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
