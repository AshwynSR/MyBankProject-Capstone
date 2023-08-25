package com.springboot.mybank.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.mybank.entities.Account;
import com.springboot.mybank.entities.Customer;
import com.springboot.mybank.repositories.AccountRepository;
import com.springboot.mybank.repositories.CustomerRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Save the account to the repository
    @Override
    public String createAccount(Account account) {
        accountRepository.save(account);
        return "Account created successfully.";
    }
    
    // Save the new customer to the repository
    @Override
    public String addCustomer(Customer customer) {
        for (Account account : customer.getAccounts()) {
            account.setCustomer(customer);
        }

        customerRepository.save(customer);
        return "Customer and accounts added successfully.";
    }

    // Update the customer details
    @Override
    public void editCustomerDetails(Long customerId, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(customerId).orElse(null);

        if (existingCustomer != null) {
            existingCustomer.setName(updatedCustomer.getName());
            existingCustomer.setEmail(updatedCustomer.getEmail());
            existingCustomer.setAddress(updatedCustomer.getAddress());

            List<Account> updatedAccounts = updatedCustomer.getAccounts();

            // Update account details, first checking for existing accounts if not present directly add
            for (Account updatedAccount : updatedAccounts) {
                Account existingAccount = accountRepository.findById(updatedAccount.getId()).orElse(null);

                if (existingAccount != null) {
                    existingAccount.setAccountType(updatedAccount.getAccountType());
                    existingAccount.setBalance(updatedAccount.getBalance());
                    accountRepository.save(existingAccount);
                } else {
                    updatedAccount.setCustomer(existingCustomer);
                    accountRepository.save(updatedAccount);
                    existingCustomer.getAccounts().add(updatedAccount);
                }
            }
            customerRepository.save(existingCustomer);
        }
    }



    // Retrieve all customers from the repository
    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    // Retrieve the customer details by customer ID from the repository
    @Override
    public Customer getCustomerDetails(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    // Retrieve all accounts of a customer
    @Override
    public List<Account> getCustomerAccounts(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    @Override
    public String transferFunds(int from, int to, double amount) {
        Account accountFrom = accountRepository.findById((long) from).orElse(null);
        Account accountTo = accountRepository.findById((long) to).orElse(null);

        if (accountFrom == null || accountTo == null ) {
            return "ID MISMATCH";
        }

        if(accountFrom == accountTo){
            return "SOURCE AND DESTINATION CAN'T BE SAME";
        }
        if(accountFrom.getBalance() < amount) {
            return "INSUFFICIENT FUNDS";
        }
        if(amount < 1){
            return "AMOUNT CAN'T BE LESS THAN 1";
        }

        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);

        return "SUCCESS";
    }

    // Delete all customer and their accounts
    @Override
    public void deleteAll(){
        customerRepository.deleteAll();
        accountRepository.deleteAll();
    }

    // Delete the customer's accounts
    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer != null) {
            List<Account> accounts = customer.getAccounts();
            accountRepository.deleteAll(accounts);

            customerRepository.deleteById(customerId);
        }
    }

    // Delete a particular account
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
