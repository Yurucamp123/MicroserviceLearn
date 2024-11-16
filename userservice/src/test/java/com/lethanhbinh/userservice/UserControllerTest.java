package com.lethanhbinh.userservice;

import com.lethanhbinh.userservice.controller.UserController;
import com.lethanhbinh.userservice.data.User;
import com.lethanhbinh.userservice.model.UserDTO;
import com.lethanhbinh.userservice.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    private UserDTO userDTO;

    @BeforeEach
    public void setup () {
        ReflectionTestUtils.setField(userController, "userService", userService);
        user = new User(1L, "krixichan", "123", "dd06932f-fa05-4626-b387-206b79d59fe9");
        userDTO = new UserDTO(1L, "krixichan", "123", "dd06932f-fa05-4626-b387-206b79d59fe9", "token", "refreshToken");
    }

    @Test
    public void getAllUsersTest() {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userService.getAllUser()).thenReturn(userList);
        List<User> result = userController.getAllUsers();

        Assertions.assertEquals(userList, result);
    }

    @Test
    public void addUserTest () {
        when(userService.saveUser(user)).thenReturn(user);
        Assertions.assertEquals(user, userController.addUser(user));
    }

    @Test
    public void loginTest () {
        when(userService.login(userDTO.getUsername(), userDTO.getPassword())).thenReturn(userDTO);
        Assertions.assertEquals(userDTO, userController.login(userDTO));
    }
}
