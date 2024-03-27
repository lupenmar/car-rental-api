package com.lupenmar.unicorntask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lupenmar.unicorntask.model.Customer;
import com.lupenmar.unicorntask.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Bobkowic");
        testCustomer.setEmail("john@google.com");
    }

    @Test
    void getAllCustomerShouldReturnCustomers() throws Exception {
        List<Customer> allCustomers = Arrays.asList(testCustomer);
        given(customerService.getAllCustomer()).willReturn(allCustomers);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(allCustomers)));

        verify(customerService).getAllCustomer();
    }

    @Test
    void addNewCustomerShouldAddCustomer() throws Exception {
        Customer customerToBeAdded = new Customer();

        mockMvc.perform(post("/api/v1/customers/add_customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerToBeAdded)))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer successfully added"));

        verify(customerService).addNewCustomer(any(Customer.class));
    }

}
