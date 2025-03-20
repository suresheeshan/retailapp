package com.cg.retailapp.service;

import com.cg.retailapp.dto.RewardPoints;
import com.cg.retailapp.model.Transaction;
import com.cg.retailapp.repository.CustomerRepository;
import com.cg.retailapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RewardService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<RewardPoints> calculateRewardPointsForLastThreeMonths(Optional<Long> customerId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(3);


        List<Transaction> transactionList;
        if (customerId.isPresent()) {
            transactionList = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId.get(), startDate, endDate);
        } else {
            transactionList = transactionRepository.findByTransactionDateBetween(startDate, endDate);
        }
        Map<Long, Map<Month, Integer>> customerRewardMap = new HashMap<>();

        for (Transaction transaction : transactionList) {
            Long custId = transaction.getCustomer().getId();
            Month transactionMonth = transaction.getTransactionDate().getMonth();
            int points = calculatePoints(transaction.getAmount());

            customerRewardMap.computeIfAbsent(custId, t -> new HashMap<>())
                    .merge(transactionMonth, points, Integer::sum);

        }

        return customerRewardMap.entrySet().stream()
                .flatMap(x -> x.getValue().entrySet().stream()
                        .map(monthEntry -> new RewardPoints(
                                customerRepository.findCustomerNameById(x.getKey()),
                                x.getKey(),
                                monthEntry.getKey(),
                                monthEntry.getValue()
                        ))).collect(Collectors.toList());

    }

    public int calculatePoints(double dollers) {
        int points = 0;

        if (dollers > 100) {
            points += (dollers - 100) * 2;
            dollers = 100;
        }
        if (dollers > 50 && dollers <= 100) {
            points += (dollers - 50);
        }
        return points;
    }


}
