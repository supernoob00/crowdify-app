package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class NewCampaignDto {
    private String name;
    private String description;
    private int fundingGoal;
    private int creatorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    @NotNull
    private boolean isPublic;

    public NewCampaignDto() {
    }

    public NewCampaignDto(String name, String description, int fundingGoal, int creatorId, LocalDateTime startDate, LocalDateTime endDate, boolean isPublic) {
        this.name = name;
        this.description = description;
        this.fundingGoal = fundingGoal;
        this.creatorId = creatorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPublic = isPublic;
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

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
