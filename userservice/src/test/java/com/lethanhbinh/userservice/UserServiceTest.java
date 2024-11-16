package com.lethanhbinh.userservice;

import com.lethanhbinh.userservice.data.User;
import com.lethanhbinh.userservice.data.UserRepository;
import com.lethanhbinh.userservice.model.UserDTO;
import com.lethanhbinh.userservice.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setup () {
        user = new User(1L, "bebuom", "123", "dd06932f-fa05-4626-b387-206b79d59fe9");
        userDTO = new UserDTO(1L, "bebuom", "123", "dd06932f-fa05-4626-b387-206b79d59fe9", "token", "refreshToken");
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @Test
    void getAllUsersTest () {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);
        List<User> result = userService.getAllUser();

        Assertions.assertEquals(userList, result);
    }

    @Test
    void addUserTest () {
        when(passwordEncoder.encode(user.getPassword())).thenReturn("Test password");
        when(userRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(user, userService.saveUser(user));
    }

    @Test
    void loginSuccessTest () {
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        Assertions.assertNotNull(userService.login(userDTO.getUsername(), userDTO.getPassword()));
    }

    @Test
    void loginWithUserNullTest () {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        Assertions.assertNull(userService.login(userDTO.getUsername(), userDTO.getPassword()));
    }

    @Test
    void loginWithUserNotMatch () {
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        Assertions.assertNull(userService.login(userDTO.getUsername(), userDTO.getPassword()));
    }
}
