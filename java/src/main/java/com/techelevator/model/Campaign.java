package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Campaign {

    private int id;
    private String name;
    private String description;
    private int fundingGoal;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private boolean locked;
    @NotNull
    private boolean isPublic;
    private List<Donation> donations = new ArrayList<>();
    private List<User> managers = new ArrayList<>();

    public Campaign() {
    }

    public Campaign(int id, String name, String description, int fundingGoal, LocalDateTime startDate, LocalDateTime endDate, boolean locked, boolean isPublic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fundingGoal = fundingGoal;
        this.startDate = startDate;
        this.endDate = endDate;
        this.locked = locked;
        this.isPublic = isPublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }

    public List<User> getManagers() {
        return managers;
    }

    public void setManagers(List<User> managers) {
        this.managers = managers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campaign campaign = (Campaign) o;
        return id == campaign.id && fundingGoal == campaign.fundingGoal && locked == campaign.locked && isPublic == campaign.isPublic && Objects.equals(name, campaign.name) && Objects.equals(description, campaign.description) && Objects.equals(startDate, campaign.startDate) && Objects.equals(endDate, campaign.endDate) && Objects.equals(donations, campaign.donations) && Objects.equals(managers, campaign.managers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, fundingGoal, startDate, endDate, locked, isPublic, donations, managers);
    }
}
