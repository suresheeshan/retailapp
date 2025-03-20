package com.cg.retailapp.service;

import com.cg.retailapp.dto.RewardPoints;
import com.cg.retailapp.model.Customer;
import com.cg.retailapp.model.Transaction;
import com.cg.retailapp.repository.CustomerRepository;
import com.cg.retailapp.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardService rewardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateRewardPointsForLastThreeMonths_ValidInput() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        Transaction transaction1 = new Transaction();
        transaction1.setCustomer(customer);
        transaction1.setAmount(new BigDecimal("120"));
        transaction1.setTransactionDate(LocalDate.now().minusDays(10));

        Transaction transaction2 = new Transaction();
        transaction2.setCustomer(customer);
        transaction2.setAmount(new BigDecimal("80"));
        transaction2.setTransactionDate(LocalDate.now().minusDays(20));

        when(transactionRepository.findByTransactionDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(transaction1, transaction2));
        when(customerRepository.findCustomerNameById(1L)).thenReturn("John Doe");

        List<RewardPoints> rewardPointsList = rewardService.calculateRewardPointsForLastThreeMonths(Optional.empty());

        assertNotNull(rewardPointsList);
        assertEquals(2, rewardPointsList.size());
        verify(transactionRepository, times(1)).findByTransactionDateBetween(any(LocalDate.class), any(LocalDate.class));
        verify(customerRepository, times(2)).findCustomerNameById(1L); // Adjusted to expect 2 invocations
    }

    @Test
    public void testCalculateRewardPointsForLastThreeMonths_CustomerIdIsValid() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setAmount(new BigDecimal("120"));
        transaction.setTransactionDate(LocalDate.now().minusDays(10));

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(eq(1L), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(transaction));
        when(customerRepository.findCustomerNameById(1L)).thenReturn("John Doe");

        List<RewardPoints> rewardPointsList = rewardService.calculateRewardPointsForLastThreeMonths(Optional.of(1L));

        assertNotNull(rewardPointsList);
        assertEquals(1, rewardPointsList.size());
        verify(transactionRepository, times(1)).findByCustomerIdAndTransactionDateBetween(eq(1L), any(LocalDate.class), any(LocalDate.class));
        verify(customerRepository, times(1)).findCustomerNameById(1L);
    }

    @Test
    public void testCalculateRewardPointsForLastThreeMonths_ErrorBehavior() {
        when(transactionRepository.findByTransactionDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            rewardService.calculateRewardPointsForLastThreeMonths(Optional.empty());
        });

        assertEquals("Database error", exception.getMessage());
        verify(transactionRepository, times(1)).findByTransactionDateBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    public void testCalculatePoints_Success() {
        BigDecimal amount = new BigDecimal("120");
        int points = rewardService.calculatePoints(amount);
        assertEquals(90, points);
    }

    @Test
    public void testCalculatePoints_Failure() {
        BigDecimal amount = new BigDecimal("40");
        int points = rewardService.calculatePoints(amount);
        assertEquals(0, points);
    }
}