package com.cg.retailapp.service;

import com.cg.retailapp.model.Customer;
import com.cg.retailapp.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCustomer_ValidInput() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        when(customerRepository.save(customer)).thenReturn(customer);

        Customer createdCustomer = customerService.createCustomer(customer);

        assertEquals("John Doe", createdCustomer.getName());
        assertEquals("john.doe@example.com", createdCustomer.getEmail());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testCreateCustomer_ErrorBehaviour() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        when(customerRepository.save(customer)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.createCustomer(customer);
        });

        assertEquals("Database error", exception.getMessage());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testGetCustomer_ValidBehaviour() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> foundCustomer = customerService.getCustomer(1L);

        assertTrue(foundCustomer.isPresent());
        assertEquals("John Doe", foundCustomer.get().getName());
        assertEquals("john.doe@example.com", foundCustomer.get().getEmail());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCustomer_Negative() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Customer> foundCustomer = customerService.getCustomer(1L);

        assertFalse(foundCustomer.isPresent());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllCustomers_ValidBehaviour() {
        Customer customer1 = new Customer();
        customer1.setName("John Doe");
        customer1.setEmail("john.doe@example.com");

        Customer customer2 = new Customer();
        customer2.setName("Jane Doe");
        customer2.setEmail("jane.doe@example.com");

        List<Customer> customerList = Arrays.asList(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(customerList);

        List<Customer> foundCustomers = customerService.getAllCustomers();

        assertNotNull(foundCustomers);
        assertEquals(2, foundCustomers.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllCustomers_ErrorBehaviour() {
        when(customerRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.getAllCustomers();
        });

        assertEquals("Database error", exception.getMessage());
        verify(customerRepository, times(1)).findAll();
    }
}
