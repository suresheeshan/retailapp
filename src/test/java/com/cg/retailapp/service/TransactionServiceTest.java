package com.cg.retailapp.service;

import com.cg.retailapp.model.Transaction;
import com.cg.retailapp.repository.TransactionRepository;
import com.cg.retailapp.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveTransaction_ValidBehaviour() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("120"));
        transaction.setTransactionDate(LocalDate.now());

        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

       // NotNull(savedTransaction);
        assertEquals(new BigDecimal("120"), savedTransaction.getAmount());
        assertEquals(LocalDate.now(), savedTransaction.getTransactionDate());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void testSaveTransaction_InValidBehaviour() {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal("120"));
        transaction.setTransactionDate(LocalDate.now());

        when(transactionRepository.save(transaction)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.saveTransaction(transaction);
        });

        assertEquals("Database error", exception.getMessage());
        verify(transactionRepository, times(1)).save(transaction);
    }
}
