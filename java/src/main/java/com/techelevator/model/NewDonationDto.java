package com.techelevator.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class NewDonationDto {

    private int donorId;
    @NotNull
    private int campaignId;
    @Positive
    private int donationAmount;
    private String donationComment;
    private String donationStatus;

    public NewDonationDto() {
    }

    public NewDonationDto(int donorId, int campaignId, int donationAmount, String donationComment, String donationStatus) {
        this.donorId = donorId;
        this.campaignId = campaignId;
        this.donationAmount = donationAmount;
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