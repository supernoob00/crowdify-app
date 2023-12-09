package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

public class NewCampaignDto {
    @NotNull @NotBlank
    private String name;
    @NotNull @NotBlank
    private String description;
    @Min(100)
    private int fundingGoal;
    @Min(1)
    private int creatorId;
    @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @NotNull @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewCampaignDto that = (NewCampaignDto) o;
        return fundingGoal == that.fundingGoal && creatorId == that.creatorId && isPublic == that.isPublic && name.equals(that.name) && description.equals(that.description) && startDate.equals(that.startDate) && endDate.equals(that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, fundingGoal, creatorId, startDate, endDate, isPublic);
    }
}
