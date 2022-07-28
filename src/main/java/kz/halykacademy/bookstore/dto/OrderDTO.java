package kz.halykacademy.bookstore.dto;

import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.enums.OrderStatus;

import java.util.List;

public class OrderDTO {
    private Long id;
    private String createdBy;
    private List<Book> books;
    private OrderStatus status;

    public OrderDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
