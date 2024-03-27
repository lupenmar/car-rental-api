package com.lupenmar.unicorntask.service;

import com.lupenmar.unicorntask.model.Car;
import com.lupenmar.unicorntask.model.RentalHistory;

import java.util.List;

public interface CarService {

    List<Car> getAllCar();

    List<RentalHistory> getAllRentalHistory();

    Car addNewCar(Car car);

    Car updateCar(Long carId, Car updatedCarInfo);

    void deleteCar(Long carId);

    RentalHistory rentCar(Long carId, Long customerId, double price);

    void returnCar(Long carId, Long customerId);

}
