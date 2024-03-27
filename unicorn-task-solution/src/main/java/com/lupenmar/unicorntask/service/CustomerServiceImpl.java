package com.lupenmar.unicorntask.service;

import com.lupenmar.unicorntask.model.Customer;
import com.lupenmar.unicorntask.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public List<Customer> getAllCustomer() {
        return repository.findAll();
    }

    @Override
    public Customer addNewCustomer(Customer customer) {
        return repository.save(customer);
    }


}
