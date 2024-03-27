package com.lupenmar.unicorntask.repository;

import com.lupenmar.unicorntask.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
