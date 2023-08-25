package com.springboot.mybank.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.mybank.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    
    Customer save(Customer customer);
    
    List<Customer> findAll();
    
    Optional<Customer> findById(Long customerId);
    
    void deleteById(Long customerId);
    
    void deleteAll();
}
