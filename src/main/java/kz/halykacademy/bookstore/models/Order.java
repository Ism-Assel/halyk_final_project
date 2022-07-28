package kz.halykacademy.bookstore.models;

import kz.halykacademy.bookstore.models.enums.OrderStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order_")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @OneToMany
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private List<Book> books;

    @Column(name = "status")
    private OrderStatus status;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createdAt;

    public Order() {
    }

    public Order(String createdBy, List<Book> books, OrderStatus status, LocalDateTime createdAt) {
        this.createdBy = createdBy;
        this.books = books;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String orderBy) {
        this.createdBy = orderBy;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
