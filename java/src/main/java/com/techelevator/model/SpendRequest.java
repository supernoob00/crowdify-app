package com.techelevator.model;

import java.time.LocalDateTime;

public class SpendRequest {

    private int id;
    private int amount;
    private String description;
    private boolean approved;
    private LocalDateTime endDate;


    public SpendRequest(int id, int amount, String description, boolean approved, LocalDateTime endDate) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.approved = approved;
        this.endDate = endDate;
    }

    public SpendRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
