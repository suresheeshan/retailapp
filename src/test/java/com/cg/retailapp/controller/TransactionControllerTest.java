package com.cg.retailapp.controller;

import com.cg.retailapp.exception.CustomerNotFoundException;
import com.cg.retailapp.model.Customer;
import com.cg.retailapp.dto.RewardPoints;
import com.cg.retailapp.model.Transaction;
import com.cg.retailapp.service.CustomerService;
import com.cg.retailapp.service.RewardService;
import com.cg.retailapp.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private CustomerService customerService;

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransaction_ValidBehaviour() throws CustomerNotFoundException {
        Customer customer = new Customer();
        customer.setId(1L);

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setAmount(new BigDecimal("120"));
        transaction.setTransactionDate(LocalDate.now());

        when(customerService.getCustomer(1L)).thenReturn(Optional.of(customer));
        when(transactionService.saveTransaction(transaction)).thenReturn(transaction);

        ResponseEntity<?> response = transactionController.createTransaction(transaction);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerService, times(1)).getCustomer(1L);
        verify(transactionService, times(1)).saveTransaction(transaction);
    }

    @Test
    public void testCreateTransaction_Negative() {
        Transaction transaction = new Transaction();
        Customer customer = new Customer();
        customer.setId(1L);
        transaction.setCustomer(customer);

        when(customerService.getCustomer(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            transactionController.createTransaction(transaction);
        });

        assertEquals("No customer available for the given id", exception.getMessage());
        verify(customerService, times(1)).getCustomer(1L);
        verify(transactionService, times(0)).saveTransaction(transaction);
    }

    @Test
    public void testGetAllTransactionRewardsForThreeMonths_ValidBehaviour() throws CustomerNotFoundException {
        RewardPoints rewardPoints = new RewardPoints("John Doe", 1L, LocalDate.now().getMonth(), 90);
        List<RewardPoints> rewardPointsList = Arrays.asList(rewardPoints);

        when(rewardService.calculateRewardPointsForLastThreeMonths(Optional.empty())).thenReturn(rewardPointsList);

        ResponseEntity<List<RewardPoints>> response = transactionController.getAllTransactionRewardsForThreeMonths(Optional.empty());

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(rewardService, times(1)).calculateRewardPointsForLastThreeMonths(Optional.empty());
    }

    @Test
    public void testGetAllTransactionRewardsForThreeMonths_InvalidBehaviour() {
        when(rewardService.calculateRewardPointsForLastThreeMonths(Optional.of(1L)))
                .thenThrow(new CustomerNotFoundException("NO Customer found for this Id"));

        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            transactionController.getAllTransactionRewardsForThreeMonths(Optional.of(1L));
        });

        assertEquals("NO Customer found for this Id", exception.getMessage());
        verify(rewardService, times(1)).calculateRewardPointsForLastThreeMonths(Optional.of(1L));
    }
}
