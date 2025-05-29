package org.example.projectmanagerapp.service;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.CarModelRequestDto;
import org.example.projectmanagerapp.entity.car.CarModel;
import org.example.projectmanagerapp.repository.CarModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarModelService {

    private final CarModelRepository carModelRepository;

    public CarModel createCarModel(CarModelRequestDto dto) {
        CarModel model = new CarModel();
        fillData(model, dto);
        return carModelRepository.save(model);
    }

    public List<CarModel> getAllCarModels() {
        return carModelRepository.findAll();
    }

    public CarModel getById(Long id) {
        return carModelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car model not found"));
    }

    public CarModel updateCarModel(Long id, CarModelRequestDto dto) {
        CarModel model = getById(id);
        fillData(model, dto);
        return carModelRepository.save(model);
    }

    public void deleteCarModel(Long id) {
        carModelRepository.deleteById(id);
    }

    private void fillData(CarModel model, CarModelRequestDto dto) {
        model.setBrand(dto.getBrand());
        model.setModel(dto.getModel());
        model.setEngineType(dto.getEngineType());
        model.setSeats(dto.getSeats());
        model.setBodyType(dto.getBodyType());
        model.setProductionYear(dto.getProductionYear());
    }
}
