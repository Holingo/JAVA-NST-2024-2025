package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.enums.UserRole;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "User already exists";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER); // default role in project
        userRepository.save(user);

        return "User registered successfully";
    }
}
