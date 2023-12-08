package com.techelevator.model;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import java.util.Objects;

public class NewVoteDto {
    @Min(1)
    private int donor_id;
    @Min(1)
    private int request_id;
    @Nullable
    private boolean vote_approved;

    public NewVoteDto(int donor_id, int request_id, boolean vote_approved) {
        this.donor_id = donor_id;
        this.request_id = request_id;
        this.vote_approved = vote_approved;
    }

    public NewVoteDto() {
    }

    public int getDonor_id() {
        return donor_id;
    }

    public void setDonor_id(int donor_id) {
        this.donor_id = donor_id;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public boolean isVote_approved() {
        return vote_approved;
    }

    public void setVote_approved(boolean vote_approved) {
        this.vote_approved = vote_approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewVoteDto that = (NewVoteDto) o;
        return donor_id == that.donor_id && request_id == that.request_id && vote_approved == that.vote_approved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(donor_id, request_id, vote_approved);
    }
}
