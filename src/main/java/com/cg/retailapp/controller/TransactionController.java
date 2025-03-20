package com.cg.retailapp.controller;

import com.cg.retailapp.exception.CustomerNotFoundException;
import com.cg.retailapp.model.Customer;
import com.cg.retailapp.dto.RewardPoints;
import com.cg.retailapp.model.Transaction;
import com.cg.retailapp.service.CustomerService;
import com.cg.retailapp.service.RewardService;
import com.cg.retailapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RewardService rewardService;

    @PostMapping("/create-transaction")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) throws CustomerNotFoundException {

     Optional<Customer> customer = customerService.getCustomer(transaction.getCustomer().getId());
     Transaction transaction1 = null;
     if(customer.isPresent()){
         transaction1 = transactionService.saveTransaction(transaction);
     }else{
         throw new CustomerNotFoundException("No customer available for the given id");
     }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<RewardPoints>> getAllTransactionRewardsForThreeMonths(@RequestParam(required = false) Optional<Long> customerId) throws CustomerNotFoundException{
        List<RewardPoints> rewardPointsList = rewardService.calculateRewardPointsForLastThreeMonths(Optional.ofNullable(customerId).orElseThrow(()-> new CustomerNotFoundException("NO Customer found for this Id")));
        return ResponseEntity.ok(rewardPointsList);
    }
}
