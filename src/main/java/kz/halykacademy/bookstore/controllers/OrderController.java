package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.OrderDTO;
import kz.halykacademy.bookstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
        return orderService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody OrderDTO orderDTO) {
        return orderService.create(orderDTO);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody OrderDTO orderDTO) {
       return orderService.update(orderDTO.getId(), orderDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        return orderService.delete(id);
    }
}
