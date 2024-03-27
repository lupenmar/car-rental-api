package com.lupenmar.unicorntask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lupenmar.unicorntask.model.Car;
import com.lupenmar.unicorntask.model.Customer;
import com.lupenmar.unicorntask.model.RentalHistory;
import com.lupenmar.unicorntask.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;
    private Car testCar;
    private RentalHistory testRentalHistory;

    @BeforeEach
    void setUp() {
        this.testCar = new Car();
        testCar.setId(1L);
        testCar.setBrand("Toyota");
        testCar.setModel("Corolla");
        testCar.setYear(2020);
        testCar.setColor("Blue");

        Customer testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Bobkowic");
        testCustomer.setEmail("johndoe@google.com");

        testRentalHistory = new RentalHistory();
        testRentalHistory.setId(1L);
        testRentalHistory.setCar(testCar);
        testRentalHistory.setCustomer(testCustomer);
        testRentalHistory.setPrice(100.0);
        testRentalHistory.setRentalDate(LocalDate.now());
        testRentalHistory.setReturnDate(LocalDate.now().plusDays(5));
    }

    @Test
    void getAllCarShouldReturnCars() throws Exception {
        List<Car> allCars = Arrays.asList(testCar);
        given(carService.getAllCar()).willReturn(allCars);

        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(allCars)));
    }

    @Test
    void getRentalHistoryShouldReturnHistory() throws Exception {
        List<RentalHistory> rentalHistories = Arrays.asList(testRentalHistory);
        given(carService.getAllRentalHistory()).willReturn(rentalHistories);

        mockMvc.perform(get("/api/v1/cars/rental_history"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rentalHistories)));
    }

    @Test
    void addNewCarShouldAddCar() throws Exception {
        String carJson = objectMapper.writeValueAsString(testCar);

        mockMvc.perform(post("/api/v1/cars/add_car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Car successfully added"));

        verify(carService, times(1)).addNewCar(any(Car.class));
    }


    @Test
    void rentCarShouldRentCarSuccessfully() throws Exception {
        HashMap<String, Double> requestBody = new HashMap<>();
        requestBody.put("price", 100.0);
        when(carService.rentCar(anyLong(), anyLong(), anyDouble())).thenReturn(testRentalHistory);

        mockMvc.perform(post("/api/v1/cars/rent_car/{carId}/{customerId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Car successfully rented")));

        verify(carService, times(1)).rentCar(anyLong(), anyLong(), anyDouble());
    }

    @Test
    void returnCarShouldReturnSuccessfully() throws Exception {
        Long carId = 1L;
        Long customerId = 1L;

        doNothing().when(carService).returnCar(carId, customerId);

        mockMvc.perform(post("/api/v1/cars/return_car/{carId}/{customerId}", carId, customerId))
                .andExpect(status().isOk())
                .andExpect(content().string("Car successfully returned"));

        verify(carService).returnCar(carId, customerId);
    }

    @Test
    void updateCarShouldUpdateSuccessfully() throws Exception {
        Long carId = 1L;
        Car updatedCarInfo = new Car();

        when(carService.updateCar(eq(carId), any(Car.class))).thenReturn(updatedCarInfo);

        mockMvc.perform(put("/api/v1/cars/update_car/{carId}", carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCarInfo)))
                .andExpect(status().isOk())
                .andExpect(content().string("Car information updated successfully"));

        verify(carService).updateCar(eq(carId), any(Car.class));
    }

    @Test
    void deleteCarShouldDeleteSuccessfully() throws Exception {
        Long carId = 1L;

        doNothing().when(carService).deleteCar(carId);

        mockMvc.perform(delete("/api/v1/cars/delete_car/{carId}", carId))
                .andExpect(status().isOk())
                .andExpect(content().string("Car successfully deleted"));

        verify(carService).deleteCar(carId);
    }
}