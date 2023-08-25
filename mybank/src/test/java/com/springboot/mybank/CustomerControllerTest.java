package com.springboot.mybank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.mybank.controllers.CustomerController;
import com.springboot.mybank.entities.Account;
import com.springboot.mybank.entities.Customer;
import com.springboot.mybank.services.AccountServiceImpl;


@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountServiceImpl accountService;

    // testing to get all customers
    @Test
    public void testGetAllCustomers() throws Exception {
        
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "Ravi Kumar", "ravi@example.com", "delhi", new ArrayList<>()));
        customers.add(new Customer(2L, "Aarav Patel", "aarav@example.com", "kanpur", new ArrayList<>()));
        Mockito.when(accountService.getCustomers()).thenReturn(customers);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Ravi Kumar")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Aarav Patel")));
    }

    // testing to add a customer
    @Test
    public void testAddCustomer() throws Exception {
       
        Customer customer = new Customer(1L, "Ravi Kumar", "ravi@example.com", "delhi", new ArrayList<>());

        Mockito.when(accountService.addCustomer(Mockito.any(Customer.class))).thenReturn("Customer and accounts added successfully.");

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Customer and accounts added successfully."));
    }

    // testing to get a particular customer detail
    @Test
    public void testGetCustomerDetails() throws Exception {
        
        Long customerId = 1L;
        Customer customer = new Customer(customerId, "Ravi Kumar", "ravi@example.com", "delhi", new ArrayList<>());
        Mockito.when(accountService.getCustomerDetails(customerId)).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}", customerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Ravi Kumar")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("ravi@example.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", Matchers.is("delhi")));
    }

    // testing to edit a customer detail
    @Test
    public void testEditCustomerDetails() throws Exception {
    
        Customer customer = new Customer(1L, "Ashwin", "ashwin@example.com", "mumbai", new ArrayList<>());

        Customer updatedCustomer = new Customer(1L, "Rahul", "rahul@example.com", "pune", new ArrayList<>());

        Mockito.when(accountService.getCustomerDetails(Mockito.anyLong())).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.put("/customers/{customerId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedCustomer)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Customer details updated successfully."));
    }

    // testing to get all accounts of a customer
    @Test
    public void testGetCustomerAccounts() throws Exception {
        
        Long customerId = 1L;
        List<Account> accounts = new ArrayList<>();
        Customer customer = new Customer(customerId, "Ravi Kumar", "ravi@example.com", "123 Main St", accounts);
        accounts.add(new Account(1L, "Savings", 5000.0, customer));
        accounts.add(new Account(2L, "Checking", 2500.0, customer));
        Mockito.when(accountService.getCustomerAccounts(customerId)).thenReturn(accounts);

        mockMvc.perform(MockMvcRequestBuilders.get("/customers/{customerId}/accounts", customerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountType", Matchers.is("Savings")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance", Matchers.is(5000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].accountType", Matchers.is("Checking")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].balance", Matchers.is(2500.0)));
    }

    // testing to transfer funds 
    @Test
    public void testTransferFunds() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/transfer-funds")
                .param("fromAccountId", "1")
                .param("toAccountId", "2")
                .param("amount", "2000.0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("")));
    }

    // testing to delete a customer
    @Test
    public void testDeleteCustomer() throws Exception {
        Long customerId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/{customerId}", customerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Customer and their accounts deleted successfully.")));
    }

    // testing to delete all customers
    @Test
    public void testDeleteAllCustomers() throws Exception {
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("All customers and their accounts deleted successfully."));
    }

    // testing to delete a customer's accounts
    @Test
    public void testDeleteCustomerAccounts_Success() throws Exception {
        Long customerId = 1L;

        List<Account> accounts = Arrays.asList(
                new Account(1L,  "Savings", 1000.0, null),
                new Account(2L,  "Checking", 500.0, null)
        );

        Mockito.when(accountService.getCustomerAccounts(Mockito.eq(customerId))).thenReturn(accounts);

        mockMvc.perform(MockMvcRequestBuilders.delete("/customers/{customerId}/accounts", customerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("All accounts of the customer deleted successfully"));
    }

    // testing to add a account to the customer
    @Test
    public void testCreateAccountForCustomer_Success() throws Exception {
        // Create a sample customer
        Customer customer = new Customer(1L, "Ashwin", "ashwin@example.com", "mumbai", new ArrayList<>());

        // Create a sample account
        Account account = new Account(1L,  "Savings", 1000.0, customer);

        // Mock the service method behavior
        Mockito.when(accountService.getCustomerDetails(Mockito.anyLong())).thenReturn(customer);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/customers/{customerId}/accounts", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(account)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Account added successfully"));

    }

    // Utility method to convert an object to JSON string
    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

