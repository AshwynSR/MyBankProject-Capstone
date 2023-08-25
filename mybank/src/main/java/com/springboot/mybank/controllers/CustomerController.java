package com.springboot.mybank.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.mybank.entities.Account;
import com.springboot.mybank.entities.Customer;
import com.springboot.mybank.services.AccountServiceImpl;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    @Autowired
    private AccountServiceImpl accountService;

    // Get all customers
    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = accountService.getCustomers();
        if (customers.isEmpty()) {
            String message = "No customers found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        return ResponseEntity.ok(customers);
    }

    // Add a customer
    @PostMapping
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
        String response = accountService.addCustomer(customer);
        return ResponseEntity.ok(response);
    }
    
    // Get single customer
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerDetails(@PathVariable Long customerId) {
        Customer customer = accountService.getCustomerDetails(customerId);
        if (customer == null) {
            String message = "No customer with that ID present";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(customer);
    }

    // Update a customer
    @PutMapping("/{customerId}")
    public ResponseEntity<String> editCustomerDetails(
            @PathVariable Long customerId,
            @RequestBody Customer customer) {
        Customer customer1 = accountService.getCustomerDetails(customerId);
        if (customer1 == null) {
            String message = "No customer with that ID present";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        
        accountService.editCustomerDetails(customerId, customer);
        return ResponseEntity.ok("Customer details updated successfully.");
    }
    
    // Get accounts of a customer
    @GetMapping("/{customerId}/accounts")
    public ResponseEntity<?> getCustomerAccounts(@PathVariable Long customerId) {
        List<Account> accounts = accountService.getCustomerAccounts(customerId);
        if(accounts == null){
            String message = "No accounts present for the customer";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(accounts);
    }

    // Add account to a customer
    @PostMapping("/{customerId}/accounts")
    public ResponseEntity<String> createAccountForCustomer(
            @PathVariable Long customerId,
            @RequestBody Account account) {

        Customer customer = accountService.getCustomerDetails(customerId);

        if (customer == null) {
            String message = "No customer with that ID present";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        
        account.setCustomer(customer);
        account.getCustomer().setId(customerId);
        
        customer.getAccounts().add(account);

        accountService.addCustomer(customer);

        return ResponseEntity.ok("Account added successfully");
    }

    // Delete all accounts of a customer
    @DeleteMapping("/{customerId}/accounts")
    public ResponseEntity<String> deleteCustomerAccounts(@PathVariable Long customerId) {
        List<Account> accounts = accountService.getCustomerAccounts(customerId);

        if (accounts == null || accounts.isEmpty()) {
            String message = "No accounts present for the customer";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        // Delete each account
        for (Account account : accounts) {
            accountService.deleteAccount(account.getId());
        }

        String message = "All accounts of the customer deleted successfully";
        return ResponseEntity.ok(message);
    }


    // Transfer funds on the basis of Id of the account
    @PostMapping("/transfer-funds")
    public ResponseEntity<String> transferFunds(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam double amount) {

        String response = accountService.transferFunds(fromAccountId.intValue(), toAccountId.intValue(), amount);
        return ResponseEntity.ok(response);
    }

    // Delete all customers
    @DeleteMapping
    public ResponseEntity<String> deleteAllCustomers(){
        accountService.deleteAll();
        return ResponseEntity.ok("All customers and their accounts deleted successfully.");
    }

    // Delete a customer
    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        accountService.deleteCustomer(customerId);
        return ResponseEntity.ok("Single Customer and their accounts deleted successfully.");
    }

}
