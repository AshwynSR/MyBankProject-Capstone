package com.springboot.mybank.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.mybank.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
    
    List<Account> findByCustomerId(Long customerId);
    
    Account save(Account account);
    
    Optional<Account> findById(Long accountId);
    
    void deleteById(Long accountId);
}
