package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Conditional;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.util.Objects;

public class NewDonationDto {
    @Min(1) @Nullable
    private Integer donorId;
    @Min(1)
    private int campaignId;
    @Min(1)
    private int amount;
    @NotNull @NotBlank @Length(max = 10000)
    private String comment;
    private boolean anonymous;

    public NewDonationDto() {
    }

    public NewDonationDto(Integer donorId, int campaignId, int amount,
                          String comment, boolean refunded, boolean anonymous) {
        this.donorId = donorId;
        this.campaignId = campaignId;
        this.amount = amount;
        this.comment = comment;
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

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }


}