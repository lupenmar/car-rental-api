package com.lupenmar.unicorntask.repository;

import com.lupenmar.unicorntask.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    void deleteById(Long id);
}
