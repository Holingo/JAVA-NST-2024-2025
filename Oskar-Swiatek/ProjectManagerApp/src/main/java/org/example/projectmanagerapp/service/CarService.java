package org.example.projectmanagerapp.service;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.CarRequestDto;
import org.example.projectmanagerapp.entity.car.Car;
import org.example.projectmanagerapp.entity.car.CarModel;
import org.example.projectmanagerapp.repository.CarModelRepository;
import org.example.projectmanagerapp.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarModelRepository carModelRepository;
    private final CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    public Car createCar(CarRequestDto dto) {
        CarModel model = carModelRepository.findById(dto.getModelId())
                .orElseThrow(() -> new RuntimeException("Car model not found"));

        Car car = new Car();
        car.setCarModel(model);
        car.setRegistrationNumber(dto.getRegistrationNumber());
        car.setPricePerDay(dto.getPricePerDay());
        car.setAvailable(dto.isAvailable());

        return carRepository.save(car);
    }

    public Car updateCar(Long id, Car updatedCar) {
        Car existing = getCarById(id);
        existing.setCarModel(updatedCar.getCarModel());
        existing.setCarModel(updatedCar.getCarModel());
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
