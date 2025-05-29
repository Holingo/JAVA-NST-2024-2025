package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.CarModelRequestDto;
import org.example.projectmanagerapp.entity.car.CarModel;
import org.example.projectmanagerapp.service.CarModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Car Models", description = "Operations related to vehicle models")
@RestController
@RequestMapping("/admin/car-models")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CarModelController {

    private final CarModelService carModelService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new car model")
    @PostMapping
    public ResponseEntity<CarModel> createCarModel(@RequestBody CarModelRequestDto dto) {
        return ResponseEntity.ok(carModelService.createCarModel(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Show all car models")
    @GetMapping
    public ResponseEntity<List<CarModel>> getAllCarModels() {
        return ResponseEntity.ok(carModelService.getAllCarModels());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Retrieve a car model by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CarModel> getById(@PathVariable Long id) {
        return ResponseEntity.ok(carModelService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing car model")
    @PutMapping("/{id}")
    public ResponseEntity<CarModel> updateCarModel(@PathVariable Long id, @RequestBody CarModelRequestDto dto) {
        return ResponseEntity.ok(carModelService.updateCarModel(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a car model by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarModel(@PathVariable Long id) {
        carModelService.deleteCarModel(id);
        return ResponseEntity.noContent().build();
    }
}
