package org.example.projectmanagerapp.service;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.UserRegistrationDto;
import org.example.projectmanagerapp.entity.enums.UserRole;
import org.example.projectmanagerapp.entity.user.*;
import org.example.projectmanagerapp.repository.CompanyUserRepository;
import org.example.projectmanagerapp.repository.PrivateUserRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PrivateUserRepository privateUserRepository;
    private final CompanyUserRepository companyUserRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDetails.getUsername());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    // For registerUser:
    public User registerUser(UserRegistrationDto dto) {
        switch (dto.getUserType()) {
            case PRIVATE -> {
                PrivateUser user = new PrivateUser();
                user.setUsername(dto.getUsername());
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                user.setRole(UserRole.USER);
                user.setFirstName(dto.getFirstName());
                user.setLastName(dto.getLastName());
                user.setEmail(dto.getEmail());
                user.setGender(dto.getGender());
                user.setDateOfBirth(dto.getDateOfBirth());
                user.setPhoneNumber(dto.getPhoneNumber());
                user.setPesel(dto.getPesel());
                user.setIdCardNumber(dto.getIdCardNumber());
                user.setDrivingLicenseNumber(dto.getDrivingLicenseNumber());
                user.setVerified(false);
                user.setAddress(dto.getAddress());
                return privateUserRepository.save(user);
            }
            case COMPANY -> {
                CompanyUser user = new CompanyUser();
                user.setUsername(dto.getUsername());
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                user.setRole(UserRole.USER);
                user.setCompanyName(dto.getCompanyName());
                user.setTaxId(dto.getTaxId());
                user.setRegon(dto.getRegon());
                user.setKrs(dto.getKrs());
                user.setContactPerson(dto.getContactPerson());
                user.setContactEmail(dto.getContactEmail());
                user.setContactPhone(dto.getContactPhone());
                user.setCompanyAddress(dto.getAddress());
                return companyUserRepository.save(user);
            }
            default -> throw new RuntimeException("Invalid user type");
        }
    }
}
