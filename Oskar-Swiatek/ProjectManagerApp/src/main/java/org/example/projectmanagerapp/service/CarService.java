package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.car.Car;
import org.example.projectmanagerapp.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    public Car createCar(Car car) {
        if (carRepository.findByRegistrationNumber(car.getRegistrationNumber()).isPresent()) {
            throw new RuntimeException("Car with this registration number already exists.");
        }
        return carRepository.save(car);
    }

    public Car updateCar(Long id, Car updatedCar) {
        Car existing = getCarById(id);
        existing.setBrand(updatedCar.getBrand());
        existing.setModel(updatedCar.getModel());
        existing.setRegistrationNumber(updatedCar.getRegistrationNumber());
        existing.setAvailable(updatedCar.isAvailable());
        return carRepository.save(existing);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByAvailableTrue();
    }
}
