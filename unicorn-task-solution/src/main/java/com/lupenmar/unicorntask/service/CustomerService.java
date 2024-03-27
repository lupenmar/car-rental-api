package com.lupenmar.unicorntask.service;

import com.lupenmar.unicorntask.model.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomer();

    Customer addNewCustomer(Customer customer);

}
