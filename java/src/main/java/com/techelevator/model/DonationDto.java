package com.techelevator.model;

import java.sql.Timestamp;

public class DonationDto {

    private int donorId;
    private int campaignId;
    private int donationAmount;
    private Timestamp donationDate;
    // TODO do we want the TS generated on the front end or as a default in the DB?
    private String donationComment;
    private String donationStatus;

    public DonationDto() {
    }

    public DonationDto(int donorId, int campaignId, int donationAmount, Timestamp donationDate, String donationComment, String donationStatus) {
        this.donorId = donorId;
        this.campaignId = campaignId;
        this.donationAmount = donationAmount;
        this.donationDate = donationDate;
        this.donationComment = donationComment;
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

    public int getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(int donationAmount) {
        this.donationAmount = donationAmount;
    }

    public Timestamp getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(Timestamp donationDate) {
        this.donationDate = donationDate;
    }

    public String getDonationComment() {
        return donationComment;
    }

    public void setDonationComment(String donationComment) {
        this.donationComment = donationComment;
    }

    public String getDonationStatus() {
        return donationStatus;
    }

    public void setDonationStatus(String donationStatus) {
        this.donationStatus = donationStatus;
    }
}