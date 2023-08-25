package com.springboot.mybank.services;

import java.util.List;

import com.springboot.mybank.entities.Account;
import com.springboot.mybank.entities.Customer;

public interface AccountService {
    
    String createAccount(Account account);

    void editCustomerDetails(Long customerId, Customer updatedCustomer);

    List<Customer> getCustomers();

    Customer getCustomerDetails(Long customerId);

    String transferFunds(int from, int to, double amount);

    String addCustomer(Customer customer);

    List<Account> getCustomerAccounts(Long customerId);

    void deleteCustomer(Long customerId);

    void deleteAll();
    
}
