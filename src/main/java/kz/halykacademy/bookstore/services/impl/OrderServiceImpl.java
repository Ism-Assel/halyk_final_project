package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.models.Order;
import kz.halykacademy.bookstore.repositories.OrderRepository;
import kz.halykacademy.bookstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void create(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Order readById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> readAll() {
        return orderRepository.findAll();
    }

    @Override
    public void update(Long id, Order updatedOrder) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            orderRepository.save(updatedOrder);
        }
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
