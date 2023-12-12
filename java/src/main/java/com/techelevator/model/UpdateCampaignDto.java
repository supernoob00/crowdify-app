package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

public class UpdateCampaignDto {
    @JsonProperty("id")
    private int campaignId; // TODO: delete this
    @NotNull @NotBlank @Length(max=50)
    private String name;
    @NotBlank @Length(max=500)
    private String description;
    @Min(100)
    private int fundingGoal;
    @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // TODO: add start date to update, only if before start date
    private LocalDateTime endDate;
    private boolean locked;
    private boolean isPublic;

    public UpdateCampaignDto(int campaignId, String name, String description, int fundingGoal, LocalDateTime startDate, LocalDateTime endDate, boolean locked, boolean isPublic) {
        this.campaignId = campaignId;
        this.name = name;
        this.description = description;
        this.fundingGoal = fundingGoal;
        this.endDate = endDate;
        this.locked = locked;
        this.isPublic = isPublic;
    }

    public UpdateCampaignDto() {
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFundingGoal() {
        return fundingGoal;
    }

    public void setFundingGoal(int fundingGoal) {
        this.fundingGoal = fundingGoal;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public String toString() {
        return "UpdateCampaignDto{" +
                "campaignId=" + campaignId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", fundingGoal=" + fundingGoal +
                ", endDate=" + endDate +
                ", locked=" + locked +
                ", isPublic=" + isPublic +
                '}';
    }
}
