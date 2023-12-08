package com.techelevator.model;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import java.util.Objects;

public class NewVoteDto {
    @Min(1)
    private int donorId;
    @Min(1)
    private int requestId;
    @Nullable
    private boolean voteApproved;

    public NewVoteDto(int donorId, int requestId, boolean voteApproved) {
        this.donorId = donorId;
        this.requestId = requestId;
        this.voteApproved = voteApproved;
    }

    public NewVoteDto() {
    }

    public int getDonorId() {
        return donorId;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public boolean isVoteApproved() {
        return voteApproved;
    }

    public void setVoteApproved(boolean voteApproved) {
        this.voteApproved = voteApproved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewVoteDto that = (NewVoteDto) o;
        return donorId == that.donorId && requestId == that.requestId && voteApproved == that.voteApproved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(donorId, requestId, voteApproved);
    }
}
