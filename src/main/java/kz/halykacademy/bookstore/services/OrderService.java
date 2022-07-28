package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.models.Order;

import java.util.List;

public interface OrderService {
    void create(Order order);

    Order readById(Long id);

    List<Order> readAll();

    void update(Long id, Order updatedOrder);

    void delete(Long id);
}
