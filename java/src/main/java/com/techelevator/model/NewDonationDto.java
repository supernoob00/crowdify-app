package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class NewDonationDto {

    @NotNull
    private int donorId;
    @NotNull
    private int campaignId;
    @Positive
    private int amount;
    private String comment;
    private String donationStatus;

    public NewDonationDto() {
    }

    public NewDonationDto(int donorId, int campaignId, int amount, String comment, String donationStatus) {
        this.donorId = donorId;
        this.campaignId = campaignId;
        this.amount = amount;
        this.comment = comment;
        this.donationStatus = donationStatus;

    }

    public int getDonorId() {
        return donorId;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDonationStatus() {
        return donationStatus;
    }

    public void setDonationStatus(String donationStatus) {
        this.donationStatus = donationStatus;
    }
}