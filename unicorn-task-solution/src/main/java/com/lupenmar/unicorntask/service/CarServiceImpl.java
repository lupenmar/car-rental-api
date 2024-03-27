package com.lupenmar.unicorntask.service;

import com.lupenmar.unicorntask.model.Car;
import com.lupenmar.unicorntask.model.Customer;
import com.lupenmar.unicorntask.model.RentalHistory;
import com.lupenmar.unicorntask.repository.CarRepository;
import com.lupenmar.unicorntask.repository.CustomerRepository;
import com.lupenmar.unicorntask.repository.RentalHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final RentalHistoryRepository rentalHistoryRepository;

    @Override
    public List<Car> getAllCar() {
        return carRepository.findAll();
    }

    @Override
    public List<RentalHistory> getAllRentalHistory() {
        return rentalHistoryRepository.findAll();
    }

    @Override
    public Car addNewCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public RentalHistory rentCar(Long carId, Long customerId, double price) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!car.isRented()) {
            car.setRented(true);
            car.setRentedBy(customer);

            LocalDate rentalDate = LocalDate.now();
            RentalHistory rentalHistory = new RentalHistory();
            rentalHistory.setCar(car);
            rentalHistory.setCustomer(customer);
            rentalHistory.setRentalDate(rentalDate);
            rentalHistory.setPrice(price);

            rentalHistoryRepository.save(rentalHistory);

            return rentalHistory;
        } else {
            throw new RuntimeException("Car is already rented");
        }
    }

    @Override
    public void returnCar(Long carId, Long customerId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car with id " + carId + " not found"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id " + customerId + " not found"));

        Optional<RentalHistory> optionalRentalHistory = rentalHistoryRepository
                .findByCarIdAndCustomerId(carId, customerId)
                .stream()
                .filter(history -> history.getReturnDate() == null)
                .findFirst();

        RentalHistory rentalHistory = optionalRentalHistory
                .orElseThrow(() -> new IllegalStateException("Rental history not found for the given car and customer"));

        if (rentalHistory.getRentalDate() == null) {
            throw new IllegalStateException("Rental date not found for the given car and customer");
        }

        rentalHistory.setReturnDate(LocalDate.now());
        rentalHistoryRepository.save(rentalHistory);

        car.setRented(false);
        carRepository.save(car);
    }

    public Car updateCar(Long carId, Car updatedCarInfo) {
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + carId));

        if (existingCar.isRented()) {
            throw new IllegalStateException("Cannot update a car that is currently rented");
        }

        BeanUtils.copyProperties(updatedCarInfo, existingCar, "id");
        return carRepository.save(existingCar);
    }

    @Override
    @Transactional
    public void deleteCar(Long carId) {

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + carId));

        if (car.isRented()) {
            throw new IllegalStateException("Cannot delete a car that is currently rented");
        }

        List<RentalHistory> rentalHistories = rentalHistoryRepository.findByCarId(carId);

        for (RentalHistory history : rentalHistories) {
            history.setCar(null);
            rentalHistoryRepository.save(history);
        }

        carRepository.deleteById(carId);
    }

}
