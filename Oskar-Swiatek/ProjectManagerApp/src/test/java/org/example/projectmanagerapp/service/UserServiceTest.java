package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.UserRegistrationDto;
import org.example.projectmanagerapp.entity.enums.UserRole;
import org.example.projectmanagerapp.entity.enums.UserType;
import org.example.projectmanagerapp.entity.user.*;
import org.example.projectmanagerapp.repository.CompanyUserRepository;
import org.example.projectmanagerapp.repository.PrivateUserRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PrivateUserRepository privateUserRepository;
    private CompanyUserRepository companyUserRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        privateUserRepository = mock(PrivateUserRepository.class);
        companyUserRepository = mock(CompanyUserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, privateUserRepository, companyUserRepository, passwordEncoder);
    }

    @Test
    void shouldReturnAllUsers() {
        User user1 = new PrivateUser(); user1.setUsername("jan");
        User user2 = new CompanyUser(); user2.setUsername("ola");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void shouldReturnUserById() {
        User user = new PrivateUser(); user.setId(1L); user.setUsername("jan");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals("jan", userService.getUserById(1L).getUsername());
    }

    @Test
    void shouldThrowIfUserNotFoundById() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserById(99L));
    }

    @Test
    void shouldGetUserByUsername() {
        User user = new CompanyUser(); user.setUsername("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        assertEquals("admin", userService.getByUsername("admin").getUsername());
    }

    @Test
    void shouldThrowIfUserNotFoundByUsername() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getByUsername("ghost"));
    }

    @Test
    void shouldCreateUser() {
        User user = new PrivateUser(); user.setUsername("save-me");
        when(userRepository.save(user)).thenReturn(user);
        assertEquals("save-me", userService.createUser(user).getUsername());
    }

    @Test
    void shouldUpdateUser() {
        User existing = new PrivateUser(); existing.setId(1L); existing.setUsername("old");
        User updated = new PrivateUser(); updated.setUsername("new");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);
        User result = userService.updateUser(1L, updated);
        assertEquals("new", result.getUsername());
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentUser() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateUser(100L, new PrivateUser()));
    }

    @Test
    void shouldDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new PrivateUser()));
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void shouldRegisterPrivateUser() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUserType(UserType.PRIVATE);
        dto.setUsername("jan");
        dto.setPassword("123");
        when(passwordEncoder.encode("123")).thenReturn("hashed123");
        when(privateUserRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.registerUser(dto);
        assertEquals("jan", result.getUsername());
    }

    @Test
    void shouldRegisterCompanyUser() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUserType(UserType.COMPANY);
        dto.setUsername("firma");
        dto.setPassword("456");
        when(passwordEncoder.encode("456")).thenReturn("hashed456");
        when(companyUserRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.registerUser(dto);
        assertEquals("firma", result.getUsername());
    }

    @Test
    void shouldThrowOnInvalidUserType() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUserType(null);
        assertThrows(RuntimeException.class, () -> userService.registerUser(dto));
    }
}
