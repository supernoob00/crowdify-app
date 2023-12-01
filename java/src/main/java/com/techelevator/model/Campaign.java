package com.techelevator.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Campaign {

    private int id;
    private String name;
    private String description;
    private int fundingGoal;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean locked;
    private boolean isPublic;
    private List<Donation> donations = new ArrayList<>();
    private List<User> managers = new ArrayList<>();

    public Campaign(){
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
}
