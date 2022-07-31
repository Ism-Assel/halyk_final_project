package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.OrderDTO;
import kz.halykacademy.bookstore.models.Order;
import kz.halykacademy.bookstore.repositories.OrderRepository;
import kz.halykacademy.bookstore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(OrderDTO orderDTO) {
        orderRepository.save(convertToOrder(orderDTO));
    }

    @Override
    public OrderDTO readById(Long id) {
        return convertToOrderDTO(orderRepository.findById(id).orElse(null));
    }

    @Override
    public List<OrderDTO> readAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, OrderDTO updatedOrderDTO) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            orderRepository.save(convertToOrder(updatedOrderDTO));
        }
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO convertToOrderDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    private Order convertToOrder(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }
}
