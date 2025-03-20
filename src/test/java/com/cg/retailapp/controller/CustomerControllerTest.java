package com.cg.retailapp.controller;

import com.cg.retailapp.exception.CustomerNotFoundException;
import com.cg.retailapp.model.Customer;
import com.cg.retailapp.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCustomer_ValidInput() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        when(customerService.createCustomer(customer)).thenReturn(customer);

       Customer response = customerController.createCustomer(customer).getBody();

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john.doe@example.com", response.getEmail());
        verify(customerService, times(1)).createCustomer(customer);
    }

    @Test
    public void testCreateCustomer_ErrorBehaviour() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        when(customerService.createCustomer(customer)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerController.createCustomer(customer);
        });

        assertEquals("Database error", exception.getMessage());
        verify(customerService, times(1)).createCustomer(customer);
    }

    @Test
    public void testGetCustomer_ValidBehaviour() throws CustomerNotFoundException {
        Customer customer1 = new Customer();
        customer1.setName("John Doe");
        customer1.setEmail("john.doe@example.com");

        Customer customer2 = new Customer();
        customer2.setName("Jane Doe");
        customer2.setEmail("jane.doe@example.com");

        List<Customer> customerList = Arrays.asList(customer1, customer2);

        when(customerService.getAllCustomers()).thenReturn(customerList);

        ResponseEntity<List<Customer>> response = customerController.getCustomer();

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    public void testGetCustomer_ErrorBehaviour() {
        when(customerService.getAllCustomers()).thenThrow(new CustomerNotFoundException("Exception"));

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerController.getCustomer();
        });

        assertEquals("Exception", exception.getMessage());
        verify(customerService, times(1)).getAllCustomers();
    }
}
