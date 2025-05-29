package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.CarModelRequestDto;
import org.example.projectmanagerapp.entity.car.CarModel;
import org.example.projectmanagerapp.repository.CarModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarModelServiceTest {

    @Mock
    private CarModelRepository carModelRepository;

    @InjectMocks
    private CarModelService carModelService;

    private AutoCloseable closeable;

    private CarModelRequestDto sampleDto;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        sampleDto = new CarModelRequestDto();
        sampleDto.setBrand("Toyota");
        sampleDto.setModel("Corolla");
        sampleDto.setEngineType("Petrol");
        sampleDto.setSeats(5);
        sampleDto.setBodyType("Sedan");
        sampleDto.setProductionYear(2022);
    }

    @Test
    void shouldCreateCarModel() {
        CarModel saved = new CarModel();
        when(carModelRepository.save(any(CarModel.class))).thenReturn(saved);

        CarModel result = carModelService.createCarModel(sampleDto);

        assertNotNull(result);
        verify(carModelRepository).save(any(CarModel.class));
    }

    @Test
    void shouldGetAllCarModels() {
        List<CarModel> mockList = List.of(new CarModel(), new CarModel());
        when(carModelRepository.findAll()).thenReturn(mockList);

        List<CarModel> result = carModelService.getAllCarModels();

        assertEquals(2, result.size());
        verify(carModelRepository).findAll();
    }

    @Test
    void shouldGetByIdWhenExists() {
        CarModel model = new CarModel();
        when(carModelRepository.findById(1L)).thenReturn(Optional.of(model));

        CarModel result = carModelService.getById(1L);

        assertNotNull(result);
        verify(carModelRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenNotFoundById() {
        when(carModelRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            carModelService.getById(2L);
        });

        assertEquals("Car model not found", ex.getMessage());
    }

    @Test
    void shouldUpdateCarModel() {
        CarModel existing = new CarModel();
        when(carModelRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(carModelRepository.save(any(CarModel.class))).thenReturn(existing);

        CarModel updated = carModelService.updateCarModel(1L, sampleDto);

        assertNotNull(updated);
        verify(carModelRepository).findById(1L);
        verify(carModelRepository).save(existing);
    }

    @Test
    void shouldDeleteCarModel() {
        carModelService.deleteCarModel(5L);
        verify(carModelRepository).deleteById(5L);
    }
}