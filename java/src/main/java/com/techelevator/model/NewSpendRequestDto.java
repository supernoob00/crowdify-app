package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Objects;

public class NewSpendRequestDto {
    @Min(1)
    private int campaign_id;
    @Positive
    private int amount;
    @NotBlank
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    public NewSpendRequestDto(int campaign_id, int amount, String description, LocalDateTime endDate) {
        this.campaign_id = campaign_id;
        this.amount = amount;
        this.description = description;
        this.endDate = endDate;
    }

    public NewSpendRequestDto() {
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
        NewSpendRequestDto that = (NewSpendRequestDto) o;
        return campaign_id == that.campaign_id && amount == that.amount && description.equals(that.description) && endDate.equals(that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(campaign_id, amount, description, endDate);
    }
}

