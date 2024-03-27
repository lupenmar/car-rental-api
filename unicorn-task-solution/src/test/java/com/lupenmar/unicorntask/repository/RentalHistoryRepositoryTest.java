package com.lupenmar.unicorntask.repository;

import com.lupenmar.unicorntask.model.Car;
import com.lupenmar.unicorntask.model.Customer;
import com.lupenmar.unicorntask.model.RentalHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RentalHistoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RentalHistoryRepository rentalHistoryRepository;

    private Car car;
    private Customer customer;

    @BeforeEach
    void setUp() {
        car = new Car();

        car.setModel("Test Model");
        car.setBrand("Test Brand");
        car.setYear(2020);

        car = entityManager.persist(car);

        customer = new Customer();

        customer.setName("John Johnas");
        customer.setEmail("john@gmail.com");
        customer.setPhoneNumber("111-222-333");

        customer = entityManager.persist(customer);

        RentalHistory rentalHistory = new RentalHistory();
        rentalHistory.setCar(car);
        rentalHistory.setCustomer(customer);

        entityManager.persist(rentalHistory);
        entityManager.flush();
    }

    @Test
    public void findByCarIdAndCustomerIdTest() {
        List<RentalHistory> foundHistories = rentalHistoryRepository.findByCarIdAndCustomerId(car.getId(), customer.getId());

        assertThat(foundHistories).hasSize(1);
        RentalHistory foundHistory = foundHistories.get(0);
        assertThat(foundHistory.getCar()).isEqualTo(car);
        assertThat(foundHistory.getCustomer()).isEqualTo(customer);
    }

    @Test
    public void findByCarIdTest() {
        List<RentalHistory> foundHistories = rentalHistoryRepository.findByCarId(car.getId());

        assertThat(foundHistories).hasSize(1);
        assertThat(foundHistories.get(0).getCar()).isEqualTo(car);
    }
}
