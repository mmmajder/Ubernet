package com.example.ubernet.repository;

import com.example.ubernet.model.CustomerPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {
    CustomerPayment findByUrl(String url);
}
