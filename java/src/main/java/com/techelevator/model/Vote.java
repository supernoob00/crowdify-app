package com.techelevator.model;

import javax.validation.constraints.Min;
import java.util.Objects;

public class Vote {
    @Min(1)
    private int userId;
    @Min(1)
    private int requestId;
    private boolean approved;

    public Vote() {
    }

    public Vote(int userId, int requestId, boolean approved) {
        this.userId = userId;
        this.requestId = requestId;
        this.approved = approved;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return userId == vote.userId && requestId == vote.requestId && approved == vote.approved;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, requestId, approved);
    }
}

