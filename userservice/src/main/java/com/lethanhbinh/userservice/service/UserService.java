package com.lethanhbinh.userservice.service;

import com.lethanhbinh.userservice.data.User;
import com.lethanhbinh.userservice.model.UserDTO;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    User saveUser(User user);
    UserDTO login(String username, String password);
}
