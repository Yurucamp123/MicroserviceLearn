package com.lethanhbinh.userservice.controller;

import com.lethanhbinh.userservice.data.User;
import com.lethanhbinh.userservice.model.UserDTO;
import com.lethanhbinh.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list-users")
    public List<User> getAllUsers () {
        return userService.getAllUser();
    }

    @PostMapping("/add-user")
    public User addUser (@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public UserDTO login (@RequestBody UserDTO dto) {
        return userService.login(dto.getUsername(), dto.getPassword());
    }
}
