package com.lethanhbinh.userservice.service;

import com.auth0.jwt.JWT;
import com.lethanhbinh.userservice.data.User;
import com.lethanhbinh.userservice.data.UserRepository;
import com.lethanhbinh.userservice.model.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDTO login(String username, String password) {
        User user = userRepository.findByUsername(username);
        UserDTO userDTO = new UserDTO();
        if (user != null) {
            BeanUtils.copyProperties(user, userDTO);
            if (passwordEncoder.matches(password, userDTO.getPassword())) {
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + (60 * 10000)))
                        .sign(algorithm);

                String refreshToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + (60L * 10000 * 10800)))
                        .sign(algorithm);

                userDTO.setAccessToken(accessToken);
                userDTO.setRefreshToken(refreshToken);
                return userDTO;
            }
        }
        return null;
    }
}
