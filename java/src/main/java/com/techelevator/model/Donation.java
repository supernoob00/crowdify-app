package com.techelevator.model;

import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;


public class Donation {
    @Min(1)
    private int donationId;
    @Nullable
    private User donor;
    @Min(1)
    private int campaignId;
    @NotNull
    private String campaignName;
    @Min(1) @Max(50_000_000)
    private int amount;
    @NotNull
    private LocalDateTime date;
    @NotBlank
    private String comment;
    private boolean refunded;
    private boolean anonymous;

    public Donation() {
    }

    public Donation(int donationId, User donor, int campaignId,
                    String campaignName, int amount,
                    LocalDateTime date, String comment, boolean refunded,
                    boolean anonymous) {
        this.donationId = donationId;
        this.donor = donor;
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.amount = amount;
        this.date = date;
        this.comment = comment;
        this.refunded = refunded;
        if (donor == null && !anonymous) {
            throw new IllegalArgumentException("A donation with no donor id " +
                    "must be anonymous.");
        }
        this.anonymous = anonymous;
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

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
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

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public User getDonor() {
        return donor;
    }

    public void setDonor(User donor) {
        this.donor = donor;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        if (donor == null && !anonymous) {
            throw new IllegalArgumentException("A donation with no donor id " +
                    "must be anonymous.");
        }
        this.anonymous = anonymous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donation donation = (Donation) o;
        return donationId == donation.donationId && campaignId == donation.campaignId && amount == donation.amount && refunded == donation.refunded && anonymous == donation.anonymous && donor.equals(donation.donor) && campaignName.equals(donation.campaignName) && date.equals(donation.date) && comment.equals(donation.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(donationId, donor, campaignId, campaignName, amount, date, comment, refunded, anonymous);
    }

    @Override
    public String toString() {
        return "Donation{" +
                "donationId=" + donationId +
                ", donor=" + donor +
                ", campaignId=" + campaignId +
                ", campaignName='" + campaignName + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                ", refunded=" + refunded +
                ", anonymous=" + anonymous +
                '}';
    }
}
