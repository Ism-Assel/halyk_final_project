package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.OrderDTO;
import kz.halykacademy.bookstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDTO> getAll() {
        return orderService.readAll();
    }

    @GetMapping("/{id}")
    public OrderDTO getById(@PathVariable(name = "id") Long id) {
        return orderService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody OrderDTO orderDTO) {
        orderService.create(orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody OrderDTO orderDTO) {
        orderService.update(orderDTO.getId(), orderDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        orderService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
