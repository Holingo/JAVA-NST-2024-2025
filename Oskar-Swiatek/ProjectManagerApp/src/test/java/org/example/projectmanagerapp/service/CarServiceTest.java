package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.CarRequestDto;
import org.example.projectmanagerapp.entity.car.Car;
import org.example.projectmanagerapp.entity.car.CarModel;
import org.example.projectmanagerapp.repository.CarModelRepository;
import org.example.projectmanagerapp.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    private CarRepository carRepository;
    private CarModelRepository carModelRepository;
    private CarService carService;

    @BeforeEach
    void setUp() {
        carRepository = mock(CarRepository.class);
        carModelRepository = mock(CarModelRepository.class);
        carService = new CarService(carModelRepository, carRepository);
    }

    @Test
    void shouldReturnAllCars() {
        when(carRepository.findAll()).thenReturn(List.of(new Car(), new Car()));
        List<Car> result = carService.getAllCars();
        assertEquals(2, result.size());
        verify(carRepository).findAll();
    }

    @Test
    void shouldReturnAvailableCars() {
        when(carRepository.findByAvailableTrue()).thenReturn(List.of(new Car()));
        List<Car> result = carService.getAvailableCars();
        assertEquals(1, result.size());
        verify(carRepository).findByAvailableTrue();
    }

    @Test
    void shouldGetCarById() {
        Car car = new Car(); car.setId(1L);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        Car result = carService.getCarById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowWhenCarNotFound() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> carService.getCarById(99L));
        assertTrue(ex.getMessage().contains("Car not found"));
    }

    @Test
    void shouldCreateCar() {
        CarRequestDto dto = new CarRequestDto();
        dto.setModelId(1L);
        dto.setRegistrationNumber("KR123");
        dto.setPricePerDay(BigDecimal.valueOf(100));
        dto.setAvailable(true);

        CarModel model = new CarModel();
        when(carModelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(carRepository.save(any(Car.class))).thenAnswer(i -> i.getArguments()[0]);

        Car result = carService.createCar(dto);
        assertEquals("KR123", result.getRegistrationNumber());
        assertEquals(BigDecimal.valueOf(100), result.getPricePerDay());
        assertTrue(result.isAvailable());
        assertEquals(model, result.getCarModel());
    }

    @Test
    void shouldThrowWhenCarModelNotFound() {
        CarRequestDto dto = new CarRequestDto();
        dto.setModelId(999L);
        when(carModelRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> carService.createCar(dto));
    }

    @Test
    void shouldUpdateCar() {
        CarModel model = new CarModel();
        Car existing = new Car(); existing.setId(1L);
        Car updated = new Car();
        updated.setCarModel(model);
        updated.setRegistrationNumber("ABC123");
        updated.setAvailable(true);

        when(carRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(carRepository.save(any(Car.class))).thenAnswer(i -> i.getArguments()[0]);

        Car result = carService.updateCar(1L, updated);
        assertEquals("ABC123", result.getRegistrationNumber());
        assertEquals(model, result.getCarModel());
        assertTrue(result.isAvailable());
    }

    @Test
    void shouldDeleteCar() {
        carService.deleteCar(5L);
        verify(carRepository).deleteById(5L);
    }
}