package com.techelevator.model;

import java.time.LocalDateTime;


public class Donation {
    private int donationId;
    private int donorId;
    private int campaignId;
    private int amount;
    private LocalDateTime date;
    private String comment;
    private String status;

    public Donation() {
    }

    public Donation(int donationId, int donorId, int campaignId, int amount, LocalDateTime date, String comment, String status) {
        this.donationId = donationId;
        this.donorId = donorId;
        this.campaignId = campaignId;
        this.amount = amount;
        this.date = date;
        this.comment = comment;
        this.status = status;
    }

    public int getDonationId() {
        return donationId;
    }

    public void setDonationId(int donationId) {
        this.donationId = donationId;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
