package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.car.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {
}
