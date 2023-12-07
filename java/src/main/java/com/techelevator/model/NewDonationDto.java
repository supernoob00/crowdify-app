package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Conditional;

import javax.validation.constraints.*;
import java.util.Objects;

public class NewDonationDto {

    @Min(1)
    private Integer donorId;
    @Min(1)
    private int campaignId;
    @Min(1)
    private int amount;
    private String comment;
    private boolean refunded;
    private boolean anonymous;

    public NewDonationDto() {
    }

    public NewDonationDto(Integer donorId, int campaignId, int amount,
                          String comment, boolean refunded, boolean anonymous) {
        this.donorId = donorId;
        this.campaignId = campaignId;
        this.amount = amount;
        this.comment = comment;
        this.refunded = refunded;
        if (donorId == null && !anonymous) {
            throw new IllegalArgumentException("A donation with no donor id " +
                    "must be anonymous.");
        }
        this.anonymous = anonymous;
    }

    public Integer getDonorId() {
        return donorId;
    }

    public void setDonorId(Integer donorId) {
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

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        if (donorId == null && !anonymous) {
            throw new IllegalArgumentException("A donation with no donor id " +
                    "must be anonymous.");
        }
        this.anonymous = anonymous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewDonationDto that = (NewDonationDto) o;
        return campaignId == that.campaignId && amount == that.amount && refunded == that.refunded && anonymous == that.anonymous && donorId.equals(that.donorId) && comment.equals(that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(donorId, campaignId, amount, comment, refunded, anonymous);
    }
}