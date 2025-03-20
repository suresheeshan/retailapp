package com.cg.retailapp.dto;

import org.springframework.stereotype.Component;

import java.time.Month;
@Component
public class RewardPoints {

    private Long customerId;
    private String customerName;
    private Month month;
    private int totalPoints;

    public RewardPoints() {
    }

    public RewardPoints(String customerName, Long customerId, Month month, int totalPoints) {
        this.customerName = customerName;
        this.customerId = customerId;
        this.month = month;
        this.totalPoints = totalPoints;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}
