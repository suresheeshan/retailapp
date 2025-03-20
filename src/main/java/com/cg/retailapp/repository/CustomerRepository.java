package com.cg.retailapp.repository;

import com.cg.retailapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    default String findCustomerNameById(Long id){
        return findById(id).map(Customer::getName).orElse("unknown");
    }
}