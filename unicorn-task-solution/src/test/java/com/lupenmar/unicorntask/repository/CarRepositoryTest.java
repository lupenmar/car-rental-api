package com.lupenmar.unicorntask.repository;

import com.lupenmar.unicorntask.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    private Car car;

    @BeforeEach
    void setUp() {

        car = new Car();
        car.setBrand("Test brand");
        car.setModel("Test model");
        car.setYear(2020);
        car.setColor("Black");
        car.setMileage(10000);

        car = entityManager.persistFlushFind(car);
    }

    @Test
    void deleteByIdTest() {

        Optional<Car> foundCar = carRepository.findById(car.getId());
        assertThat(foundCar).isPresent();

        carRepository.deleteById(car.getId());

        Optional<Car> deletedCar = carRepository.findById(car.getId());
        assertThat(deletedCar).isNotPresent();
    }
}
