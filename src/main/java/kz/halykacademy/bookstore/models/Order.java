package kz.halykacademy.bookstore.models;

import kz.halykacademy.bookstore.dto.book.BookResponse;
import kz.halykacademy.bookstore.dto.order.OrderResponse;
import kz.halykacademy.bookstore.dto.user.UserResponse;
import kz.halykacademy.bookstore.models.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "order_")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_book",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public OrderResponse toOrderDto() {
        List<BookResponse> bookResponses = List.of();

        if (this.books != null) {
            bookResponses = this.books.stream().map(Book::toBookDto).collect(Collectors.toList());
        }

//        UserResponse userResponse = new UserResponse( // todo вернуть в security
//                user.getId(),
//                user.getLogin(),
//                "",
//                user.getRole(),
//                user.getIsBlocked()
//        );
        UserResponse userResponse = new UserResponse();

        return new OrderResponse(
                this.id,
                bookResponses,
                this.status.getId(),
                this.createdAt,
                userResponse
        );
    }
}
