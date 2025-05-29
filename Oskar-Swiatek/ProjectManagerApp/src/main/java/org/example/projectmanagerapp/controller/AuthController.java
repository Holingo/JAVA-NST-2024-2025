package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.UserRegistrationDto;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Register", description = "Operations related to user registration")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "User registration operations")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto dto) {
        User registered = userService.registerUser(dto);
        return ResponseEntity.ok("User registered: " + registered.getUsername());
    }
}
