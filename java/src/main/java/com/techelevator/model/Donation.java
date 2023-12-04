package com.techelevator.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Objects;


public class Donation {
    @NotNull
    private int donationId;
    @NotNull
    private User donor;
    @NotNull
    private int campaignId;
    @Positive
    private int amount;
    @NotNull
    private LocalDateTime date;
    private String comment;
    private String status;

    public Donation() {
    }

    public Donation(int donationId, User donor, int campaignId, int amount, LocalDateTime date, String comment, String status) {
        this.donationId = donationId;
        this.donor = donor;
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

    public User getDonor() {
        return donor;
    }

    public void setDonor(User donor) {
        this.donor = donor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donation donation = (Donation) o;
        return donationId == donation.donationId && campaignId == donation.campaignId && amount == donation.amount && Objects.equals(donor, donation.donor) && Objects.equals(date, donation.date) && Objects.equals(comment, donation.comment) && Objects.equals(status, donation.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(donationId, donor, campaignId, amount, date, comment, status);
    }
}
