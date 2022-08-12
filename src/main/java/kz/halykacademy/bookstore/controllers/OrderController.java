package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.order.OrderAdminRequest;
import kz.halykacademy.bookstore.dto.order.OrderUserRequest;
import kz.halykacademy.bookstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity getAll() {
        return orderService.readAll();
    }

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
        return orderService.readById(id);
    }

    @PostMapping("/user")
    public ResponseEntity post(@RequestBody OrderUserRequest request) {
        return orderService.create(request);
    }

    @PostMapping("/admin")
    public ResponseEntity post(@RequestBody OrderAdminRequest request) {
        return orderService.create(request);
    }

    @PutMapping("/user")
    public ResponseEntity put(@RequestBody OrderUserRequest request) {
       return orderService.update(request.getId(), request);
    }

    @PutMapping("/admin")
    public ResponseEntity put(@RequestBody OrderAdminRequest request) {
        return orderService.update(request.getId(), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        return orderService.delete(id);
    }
}
