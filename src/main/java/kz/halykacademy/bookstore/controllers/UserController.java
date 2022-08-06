package kz.halykacademy.bookstore.controllers;

import kz.halykacademy.bookstore.dto.UserDTO;
import kz.halykacademy.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/searchById")
    public ResponseEntity getById(@RequestParam(name = "id") Long id) {
        return userService.readById(id);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody UserDTO userDTO) {
        return userService.create((userDTO));
    }

    @PutMapping
    public ResponseEntity put(@RequestBody UserDTO userDTO) {
       return userService.update(userDTO.getId(), userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {
       return userService.delete(id);
    }
}
