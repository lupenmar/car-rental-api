package com.lupenmar.unicorntask.repository;

import com.lupenmar.unicorntask.model.RentalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {
    List<RentalHistory> findByCarIdAndCustomerId(Long carId, Long customerId);

    List<RentalHistory> findByCarId(Long carId);
}
