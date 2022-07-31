package kz.halykacademy.bookstore.services;

import kz.halykacademy.bookstore.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    void create(OrderDTO orderDTO);

    OrderDTO readById(Long id);

    List<OrderDTO> readAll();

    void update(Long id, OrderDTO updatedOrderDTO);

    void delete(Long id);
}
