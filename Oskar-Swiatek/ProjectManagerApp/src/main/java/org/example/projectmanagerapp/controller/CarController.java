package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.dto.CarRequestDto;
import org.example.projectmanagerapp.entity.car.Car;
import org.example.projectmanagerapp.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cars", description = "Operations related to vehicles")
@RestController
@RequestMapping("/admin/cars")
@PreAuthorize("hasRole('ADMIN')")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Operation(summary = "Get all my cars")
    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @Operation(summary = "Retrieve a car by ID")
    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @Operation(summary = "Create a new car")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Car> createCar(@RequestBody CarRequestDto dto) {
        return ResponseEntity.ok(carService.createCar(dto));
    }

    @Operation(summary = "Update an existing car")
    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
        return carService.updateCar(id, car);
    }

    @Operation(summary = "Delete a car by ID")
    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}
