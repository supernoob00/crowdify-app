package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;

public class UpdateVoteDto {
    @Min(1)
    private int userId;
    @Min(1)
    private int requestId;
    private boolean approved;

    public UpdateVoteDto(int userId, int requestId, boolean approved) {
        this.userId = userId;
        this.requestId = requestId;
        this.approved = approved;
    }

    public UpdateVoteDto() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
