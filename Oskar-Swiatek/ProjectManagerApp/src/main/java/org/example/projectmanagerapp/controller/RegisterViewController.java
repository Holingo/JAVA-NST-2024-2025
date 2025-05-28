package org.example.projectmanagerapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.UserRegistrationDto;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegisterViewController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // resources/templates/register.html
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute UserRegistrationDto dto) {
        userService.registerUser(dto);
        return "redirect:/login"; // if success send to sign up
    }
}
