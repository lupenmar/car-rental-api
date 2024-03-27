package com.lupenmar.unicorntask.controller;

import com.lupenmar.unicorntask.model.Car;
import com.lupenmar.unicorntask.model.RentalHistory;
import com.lupenmar.unicorntask.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cars")
@AllArgsConstructor
public class CarController {

    private final CarService service;

    @Operation(summary = "Get all cars")
    @GetMapping
    public List<Car> getAllCar() {
        return service.getAllCar();
    }

    @Operation(summary = "Get rental history")
    @GetMapping("/rental_history")
    public List<RentalHistory> getRentalHistory() {
        return service.getAllRentalHistory();
    }

    @Operation(summary = "Add a new car")
    @PostMapping("add_car")
    public String addNewCar(@RequestBody Car car) {
        service.addNewCar(car);
        return "Car successfully added";
    }

    @Operation(summary = "Rent a car")
    @PostMapping("rent_car/{carId}/{customerId}")
    public ResponseEntity<String> rentCar(@PathVariable Long carId, @PathVariable Long customerId, @RequestBody Map<String, Double> requestBody) {
        try {
            Double price = requestBody.get("price");
            if (price == null) {
                throw new IllegalArgumentException("Price is missing in the request body");
            }

            RentalHistory rentalHistory = service.rentCar(carId, customerId, price);
            return ResponseEntity.ok("Car successfully rented. Price: " + rentalHistory.getPrice());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Return a rented car")
    @PostMapping("return_car/{carId}/{customerId}")
    public ResponseEntity<String> returnCar(@PathVariable Long carId, @PathVariable Long customerId) {
        try {
            service.returnCar(carId, customerId);
            return ResponseEntity.ok("Car successfully returned");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update car information")
    @PutMapping("update_car/{carId}")
    public ResponseEntity<String> updateCar(@PathVariable Long carId, @RequestBody Car updatedCarInfo) {
        try {
            service.updateCar(carId, updatedCarInfo);
            return ResponseEntity.ok("Car information updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete a car")
    @DeleteMapping("delete_car/{carId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long carId) {
        try {
            service.deleteCar(carId);
            return ResponseEntity.ok("Car successfully deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
