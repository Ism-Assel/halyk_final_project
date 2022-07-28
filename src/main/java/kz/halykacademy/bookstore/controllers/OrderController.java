package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.OrderDTO;
import kz.halykacademy.bookstore.models.Order;
import kz.halykacademy.bookstore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<OrderDTO> getAll() {
        return orderService.readAll()
                .stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDTO getById(@PathVariable(name = "id") Long id) {
        return convertToOrderDTO(orderService.readById(id));
    }

    @PostMapping
    public ResponseEntity post(@RequestBody OrderDTO orderDTO) {
        orderService.create(convertToOrder(orderDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody OrderDTO orderDTO) {
        orderService.update(orderDTO.getId(), convertToOrder(orderDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        orderService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    private OrderDTO convertToOrderDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    private Order convertToOrder(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }
}
