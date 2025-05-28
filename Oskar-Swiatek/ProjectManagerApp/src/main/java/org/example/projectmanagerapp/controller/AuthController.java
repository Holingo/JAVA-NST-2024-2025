package org.example.projectmanagerapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.UserRegistrationDto;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto dto) {
        User registered = userService.registerUser(dto);
        return ResponseEntity.ok("User registered: " + registered.getUsername());
    }
}
