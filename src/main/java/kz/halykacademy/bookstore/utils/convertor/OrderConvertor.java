package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.book.BookResponse;
import kz.halykacademy.bookstore.dto.order.OrderResponse;
import kz.halykacademy.bookstore.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConvertor {
    private final BookConvertor bookConvertor;

    @Autowired
    public OrderConvertor(BookConvertor bookConvertor) {
        this.bookConvertor = bookConvertor;
    }

    public OrderResponse toOrderDto(Order order) {
        List<BookResponse> bookResponses = List.of();

        if (order.getBooks() != null) {
            bookResponses = order.getBooks().stream().map(bookConvertor::toBookDto).collect(Collectors.toList());
        }

        return new OrderResponse(
                order.getId(),
                bookResponses,
                order.getStatus().getId(),
                order.getCreatedAt(),
                order.getUser().getLogin()
        );
    }
}