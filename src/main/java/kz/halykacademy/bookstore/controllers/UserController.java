package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.UserDTO;
import kz.halykacademy.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.readAll();
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable(name = "id") Long id) {
        return userService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody UserDTO userDTO) {
        userService.create((userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity put(@RequestBody UserDTO userDTO) {
        userService.update(userDTO.getId(), userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
