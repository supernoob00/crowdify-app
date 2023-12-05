package com.techelevator.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Objects;

public class SpendRequest {
    @Min(1)
    private int id;
    @Min(1)
    private int campaign_id;
    @Positive
    private int amount;
    @NotBlank
    private String description;
    @NotNull
    private boolean approved;
    @NotNull
    private LocalDateTime endDate;

    public SpendRequest() {
    }

    public SpendRequest(int id, int campaign_id, int amount, String description, boolean approved, LocalDateTime endDate) {
        this.id = id;
        this.campaign_id = campaign_id;
        this.amount = amount;
        this.description = description;
        this.approved = approved;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(int campaign_id) {
        this.campaign_id = campaign_id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpendRequest that = (SpendRequest) o;
        return id == that.id && campaign_id == that.campaign_id && amount == that.amount && approved == that.approved && Objects.equals(description, that.description) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, campaign_id, amount, description, approved, endDate);
    }
}
