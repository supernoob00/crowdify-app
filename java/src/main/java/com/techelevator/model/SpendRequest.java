package com.techelevator.model;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class SpendRequest {
    @Min(1)
    private int id;
    @Min(1)
    private int campaignId;
    @NotBlank
    private String requestName;
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

    public SpendRequest(int id, int campaignId, int amount, String description, boolean approved, LocalDateTime endDate) {
        this.id = id;
        this.campaignId = campaignId;
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

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
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
        SpendRequest request = (SpendRequest) o;
        return id == request.id && campaignId == request.campaignId && amount == request.amount && approved == request.approved && requestName.equals(request.requestName) && description.equals(request.description) && endDate.equals(request.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, campaignId, requestName, amount, description, approved, endDate);
    }
}
