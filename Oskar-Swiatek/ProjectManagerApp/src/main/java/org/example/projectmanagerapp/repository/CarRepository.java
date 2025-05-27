package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByRegistrationNumber(String registrationNumber);
    List<Car> findByAvailableTrue();
}
