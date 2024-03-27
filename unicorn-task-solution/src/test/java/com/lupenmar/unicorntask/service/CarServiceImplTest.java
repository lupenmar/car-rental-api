package com.lupenmar.unicorntask.service;

import com.lupenmar.unicorntask.model.Car;
import com.lupenmar.unicorntask.model.Customer;
import com.lupenmar.unicorntask.model.RentalHistory;
import com.lupenmar.unicorntask.repository.CarRepository;
import com.lupenmar.unicorntask.repository.CustomerRepository;
import com.lupenmar.unicorntask.repository.RentalHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RentalHistoryRepository rentalHistoryRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRentCarSuccessfulRent() {

        Long carId = 1L;
        Long customerId = 2L;
        double price = 100.0;
        Car car = new Car();
        car.setId(carId);
        Customer customer = new Customer();
        customer.setId(customerId);
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        RentalHistory rentalHistory = carService.rentCar(carId, customerId, price);

        assertTrue(car.isRented());
        assertEquals(customer, car.getRentedBy());
        assertEquals(LocalDate.now(), rentalHistory.getRentalDate());
        assertEquals(car, rentalHistory.getCar());
        assertEquals(customer, rentalHistory.getCustomer());
        assertEquals(price, rentalHistory.getPrice());
        verify(rentalHistoryRepository, times(1)).save(rentalHistory);
    }

    @Test
    public void testGetAllCar() {
        when(carRepository.findAll()).thenReturn(Collections.emptyList());

        carService.getAllCar();

        verify(carRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllRentalHistory() {
        when(rentalHistoryRepository.findAll()).thenReturn(Collections.emptyList());

        carService.getAllRentalHistory();

        verify(rentalHistoryRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateCar() {

        Long carId = 1L;
        Car existingCar = new Car();
        existingCar.setId(carId);
        Car updatedCarInfo = new Car();
        updatedCarInfo.setBrand("Updated brand");
        updatedCarInfo.setModel("Updated model");
        updatedCarInfo.setYear(2022);
        updatedCarInfo.setColor("Updated color");
        updatedCarInfo.setMileage(50000);
        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(existingCar)).thenReturn(updatedCarInfo);

        Car updatedCar = carService.updateCar(carId, updatedCarInfo);

        assertNotNull(updatedCar);
        assertEquals(updatedCarInfo.getBrand(), updatedCar.getBrand());
        assertEquals(updatedCarInfo.getModel(), updatedCar.getModel());
        assertEquals(updatedCarInfo.getYear(), updatedCar.getYear());
        assertEquals(updatedCarInfo.getColor(), updatedCar.getColor());
        assertEquals(updatedCarInfo.getMileage(), updatedCar.getMileage());
        verify(carRepository, times(1)).save(existingCar);
    }

    @Test
    public void testDeleteCar() {

        Long carId = 1L;
        Car existingCar = new Car();
        existingCar.setId(carId);
        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));

        carService.deleteCar(carId);

        verify(carRepository, times(1)).deleteById(carId);
    }

    @Test
    public void testRentCarAlreadyRentedByAnotherCustomer() {
        Long carId = 1L;
        Long firstCustomerId = 1L;
        Long secondCustomerId = 2L;
        Car car = new Car();
        car.setId(carId);
        car.setRented(true);
        Customer firstCustomer = new Customer();
        firstCustomer.setId(firstCustomerId);
        Customer secondCustomer = new Customer();
        secondCustomer.setId(secondCustomerId);
        double rentPrice = 100.0;

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(customerRepository.findById(firstCustomerId)).thenReturn(Optional.of(firstCustomer));
        when(customerRepository.findById(secondCustomerId)).thenReturn(Optional.of(secondCustomer));

        Exception exception = assertThrows(RuntimeException.class, () ->
                carService.rentCar(carId, secondCustomerId, rentPrice));

        String expectedMessage = "Car is already rented";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRentReturnAndRentAgainBySameCustomer() {
        Long carId = 1L;
        Long customerId = 1L;
        Car car = new Car();
        car.setId(carId);
        car.setRented(false);
        Customer customer = new Customer();
        customer.setId(customerId);
        double firstRentPrice = 100.0;

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        RentalHistory firstRental = carService.rentCar(carId, customerId, firstRentPrice);
        verify(rentalHistoryRepository, times(1)).save(any(RentalHistory.class));
        assertTrue(car.isRented());

        when(rentalHistoryRepository.findByCarIdAndCustomerId(carId, customerId))
                .thenReturn(Collections.singletonList(firstRental));
        carService.returnCar(carId, customerId);
        assertFalse(car.isRented());

        double secondRentPrice = 150.0;
        RentalHistory secondRental = carService.rentCar(carId, customerId, secondRentPrice);
        verify(rentalHistoryRepository, times(3)).save(any(RentalHistory.class));

        assertTrue(car.isRented());
        assertEquals(customer, secondRental.getCustomer());
        assertEquals(car, secondRental.getCar());
        assertEquals(secondRentPrice, secondRental.getPrice());
        assertNotNull(secondRental.getRentalDate());
    }

    @Test
    public void whenDeleteRentedCarThenThrowIllegalStateException() {

        Car car = new Car();
        car.setId(1L);
        car.setRented(true);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        assertThrows(IllegalStateException.class, () -> carService.deleteCar(1L));
    }

    @Test
    public void whenUpdateRentedCarThenThrowIllegalStateException() {

        Car car = new Car();
        car.setId(1L);
        car.setRented(true);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Car updateInfo = new Car();

        updateInfo.setBrand("Updated brand");

        assertThrows(IllegalStateException.class, () -> carService.updateCar(1L, updateInfo),
                "Cannot update a car that is currently rented");
    }

}
