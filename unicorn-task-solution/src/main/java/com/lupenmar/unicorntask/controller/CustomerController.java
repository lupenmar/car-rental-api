package com.lupenmar.unicorntask.controller;

import com.lupenmar.unicorntask.model.Customer;
import com.lupenmar.unicorntask.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@AllArgsConstructor
public class CustomerController {

    private CustomerService service;

    @GetMapping
    @Operation(summary = "Get all customers")
    public List<Customer> getAllCustomer() {
        return service.getAllCustomer();
    }

    @PostMapping("add_customer")
    @Operation(summary = "Add new customer")
    public String addNewCustomer(@RequestBody Customer customer) {
        service.addNewCustomer(customer);
        return "Customer successfully added";
    }

}
