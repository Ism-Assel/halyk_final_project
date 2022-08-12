package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.user.UserRequest;
import kz.halykacademy.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity getAll() {
        return userService.readAll();
    }

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
        return userService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody UserRequest request) {
        return userService.create((request));
    }

    @PutMapping
    public ResponseEntity put(@RequestBody UserRequest request) {
       return userService.update(request.getId(), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
       return userService.delete(id);
    }
}
