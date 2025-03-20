package com.cg.retailapp.controller;

import com.cg.retailapp.exception.CustomerNotFoundException;
import com.cg.retailapp.model.Customer;
import com.cg.retailapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create-customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomer() throws CustomerNotFoundException {
        List<Customer> customer = Optional.of(customerService.getAllCustomers()).orElseThrow(() -> new CustomerNotFoundException("Exception"));
        return ResponseEntity.ok(customer);
    }

}
