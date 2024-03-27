package com.lupenmar.unicorntask.service;

import com.lupenmar.unicorntask.model.Customer;
import com.lupenmar.unicorntask.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCustomer() {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John Bobkowic", "john@google.com", "111-222-333"));
        customers.add(new Customer(2L, "Jane Bobkowic", "jane@google.com", "123-456-789"));

        when(repository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.getAllCustomer();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testAddNewCustomer() {

        Customer customer = new Customer(null, "John Bobkowic", "john@google.com", "111-222-333");
        Customer savedCustomer = new Customer(1L, "John Bobkowic", "john@google.com", "123-456-789");

        when(repository.save(customer)).thenReturn(savedCustomer);

        Customer result = customerService.addNewCustomer(customer);

        assertEquals(savedCustomer.getId(), result.getId());
        assertEquals(savedCustomer.getName(), result.getName());
        assertEquals(savedCustomer.getEmail(), result.getEmail());
        assertEquals(savedCustomer.getPhoneNumber(), result.getPhoneNumber());
        verify(repository, times(1)).save(customer);
    }
}
